package com.zhonghong.helper;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 该工具类基于 DataSource 获取数据库连接<br>
 */
public final class JdbcHelper {

	public static void main(String[] args) {
		// 1、调用 JdbcHelper 类的 getInstance 方法来获取 JdbcHelper 实例
		JdbcHelper h = JdbcHelper.getInstance();
		// 2、执行操作 ( 插入、更新、删除、查询)
		String SQL = "insert into t_user ( username , password ) values ( ? , ? )" ;
		int count = h.update( SQL , "zhonghong" , "123456" );
		System.out.println( count );
		// 3、释放资源
		h.release();
	}
	
	/** 声明一个 DataSource 类型的属性 用来 保存 数据源 对象 */
	private DataSource dataSource ;
	
	/** 声明一个 Connection 类型的属性 用来 保存创建好的数据库连接 */
	private Connection conn ;
	
	/** 声明一个 PreparedStatement 类型的属性 用来 保存创建好的 PreparedStatement  **/
	private PreparedStatement ps ;

	private  static JdbcHelper helper;

	private JdbcHelper(){
	}

	/**
	 * 用来读取默认位置 ( 类路径下) 默认名称 ( config.properties ) 的配置文件，并返回一个 JdbcHelper 实例
	 * @return
	 */
	public static JdbcHelper getInstance() {
		helper = new JdbcHelper();
		helper.load( "/config.properties" );
		return helper ;
	}

	/**
	 * 用来读取指定位置 指定名称 的配置文件，并返回一个 JdbcHelper 示例
	 * @return
	 */
	public static JdbcHelper getInstance( String path ) {
		helper = new JdbcHelper();
		helper.load( "/" + path );
		return helper ;
	}

	/**  用于读取数据库配置文件 */
	private void load( String config ) {
		// 读取类路径下指定包中的配置文件
		InputStream in = JdbcHelper.class.getResourceAsStream( config );
		if( in == null ){
			throw new DataAccessException( "未找到配置文件[ " + config + " ]" );
		}
		Properties props = new Properties();
		try {
			props.load( in );
			dataSource = new DruidDataSource() ;
			BeanUtils.copyProperties( dataSource , props );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/** 获得数据库连接 */
	protected void connect() {
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			System.out.println( "获取数据库连接失败: " + e.getMessage() );
		}
		
	}
	
	/**
	 * 创建一个 PreparedStatement 对象，它 可以执行 参数化 SQL  也可以执行 非参数化 SQL
	 * @param SQL 需要执行的 SQL 语句
	 * @param key 根据 key 的取值决定是否需要获取由数据库产生的键，true 表示需要，false 表示不需要
	 */
	protected void prepare( String SQL , boolean key ) {
		// 检查连接，如果连接不可用
		if( isInvalid() ) {
			// 则 获得连接
			connect();
		}
		
		try {
			int generated = key ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS ;
			ps = conn.prepareStatement( SQL , generated );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 专门用来执行 INSERT 语句并返回 由数据库产生的 键
	 * @param SQL
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insert( String SQL , Object... params ) throws DataAccessException {
		
		if( SQL == null ||SQL.trim().length() == 0 ) {
			throw new DataAccessException( "你丫执行的不是INSERT语句，刚过完七夕节，能认真点吗?" );
		}
		
		if( SQL.trim().toUpperCase().startsWith( "INSERT" )  ) {
			
			// 创建一个 可以获取 由数据库产生的 键 的 PreparedStatement 对象
			prepare( SQL ,  true );
			
			// 如果 SQL 中含有参数占位符，并且 指定了可变长参数，则将参数值 设置到 相应的位置
			if( hasPlaceholder( SQL ) ) {
				setParameter( params );
			}
			
			try {
				int count = ps.executeUpdate(); // 执行 SQL 语句并返回受SQL影响的纪录数目
				int[] array = new int[ count ]; // 受影响的记录数是多少，就一定返回多少个 key
				ResultSet keys = ps.getGeneratedKeys(); // 获取由数据库产生的键
				int i = 0 ;
				while( keys.next() ) {
					int id = keys.getInt( 1 ); 
					array[ i ] = id ;
					i++ ;
				}
				return array ;
			} catch ( SQLException cause ) {
				throw new DataAccessException( "执行插入操作失败" , cause );
			}
			
			
		} else {
			throw new DataAccessException( "你丫执行的不是INSERT语句，刚过完七夕节，能认真点吗?" );
		}
		
	}
	
	/**
	 * 执行 SQL 语句 :  DML ( INSERT 、UPDATE 、 DELETE )
	 * 注意: 这里执行的 INSERT 语句不返回由数据库产生的键
	 * @param SQL 需要执行的 SQL 语句，可以是 参数化SQL ，也可以是非参数化SQL
	 * @param params 如果 第一个参数 是 参数化SQL，则需要依次指定相应的 参数值
	 * @return 返回受 当前的 DML 语句影响的记录数目
	 * @throws DataAccessException
	 */
	public int update( String SQL , Object... params ) throws DataAccessException {
		
		// 如果不是 DML 语句则抛出异常
		if( notDML( SQL ) ) {
			throw new DataAccessException( "你丫执行的不是DML语句，刚过完七夕节，能认真点吗?" );
		}
		
		// 调用 prepare 方法，创建 PreparedStatement 对象
		prepare( SQL , false );
		
		// 如果 SQL 中含有参数占位符，并且 指定了可变长参数，则将参数值 设置到 相应的位置
		if( hasPlaceholder( SQL ) ) {
			setParameter( params );
		}
		
		try {
			return ps.executeUpdate();
		} catch ( SQLException cause ) {
			throw new DataAccessException( "执行DML操作失败" , cause );
		}
		
	}

	/**
	 * 为 PreparedStatement 对象设置参数占位符的值
	 * @param params 通过可变长参数来传入 参数占位符 的值
	 */
	private void setParameter( Object... params ) {
		if( params != null && params.length > 0 ) {
			try {
				for( int i = 0 ; i < params.length ; i++ ) {
					ps.setObject( i + 1 , params[ i ] ); // 参数索引从 1 开始 ，数组下标从 0 开始
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行 SQL 语句 : DQL ( SELECT )
	 * @param SQL 需要执行的 查询语句
	 * @param params 如果被执行的SQL语句含有参数占位符，则通过可变长参数依次指定参数值
	 * @return 返回 查询语句 对应的 结果集对象
	 * @throws DataAccessException
	 */
	public ResultSet query(String SQL, Object... params) throws DataAccessException {
		
		// 如果不是 查询 语句则抛出异常
		if ( notQuery(SQL) ) {
			throw new DataAccessException("你丫执行的不是查询语句，刚过完七夕节，能认真点吗?");
		}
		
		// 调用 prepare 方法，创建 PreparedStatement 对象
		prepare( SQL , false );

		// 如果 SQL 中含有参数占位符，并且 指定了可变长参数，则将参数值 设置到 相应的位置
		if (hasPlaceholder(SQL)) {
			setParameter(params);
		}

		try {
			return ps.executeQuery();
		} catch (SQLException cause) {
			throw new DataAccessException( "查询操作失败", cause );
		}
		
	}

	/** 释放资源 */
	public void release() {
		helper = null ;
		close( ps );
		close( conn );
		System.gc();
	}
	
	/** 关闭 结果集 */
	public void release( ResultSet rs ) {
		close( rs );
	}
	
	/**
	 * 关闭一个实现过 AutoCloseable 接口的类型的对象
	 * @param ac
	 */
	private void close( AutoCloseable ac ) {
		if( ac != null ) {
			try {
				ac.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 检测连接( Connection ) 是否是废弃的(不能用的)
	 * @return 如果连接不可用则返回 true
	 */
	private boolean isInvalid() {
		try {
			return conn == null || conn.isClosed() || !conn.isValid(0)  ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false ;
	}
	
	/** 检查 SQL 语句中是否包含 参数占位符 */
	private boolean hasPlaceholder( String SQL ) {
		if( SQL != null && SQL.trim().length() >0 ) {
			return SQL.indexOf( "?" ) != -1 ;
		}
		return false ;
	}

	/**
	 * 检查将要执行的 SQL 语句是否【不】是 DML 语句
	 * @param SQL 需要检查的SQL语句
	 * @return 如果被检查的 SQL 语句不是 DML 语句则返回 true
	 */
	private boolean notDML( String SQL ) {
		
		if( SQL == null || SQL.trim().length() == 0 ) {
			// 如果是 null 或 空串 则一定不是 DML 语句
			return true ;
		}
		
		SQL = SQL.trim() ;
		SQL = SQL.toUpperCase() ;
		
		// 如果是 DML 语句
		if( SQL.startsWith( "INSERT" ) || SQL.startsWith( "UPDATE" ) || SQL.startsWith( "DELETE" ) ) {
			return false ;
		} else { // 如果不是 DML 语句
			return  true ;
		}
		
	}

	/**
	 * 检查将要执行的 SQL 语句是否 【不】是 查询 语句
	 * @param SQL
	 * @return
	 */
	private boolean notQuery( String SQL ) {
		
		if( SQL == null || SQL.trim().length() == 0 ) {
			return true ;
		}
		
		SQL = SQL.trim() ;
		SQL = SQL.toUpperCase() ;
		
		return !SQL.startsWith( "SELECT" )  ; // 注意，这里加了 感叹号
	}

	/** 关闭自动提交 */
	private void closeAutoCommit() {
		try {
			conn.setAutoCommit( false );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 启用自动提交 */
	private void enableAutoCommit() {
		try {
			conn.setAutoCommit( true );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 检查 当前 连接 对象上 事务是否是自动提交 */
	private boolean isAutoCommit() {
		try {
			return conn.getAutoCommit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false ;
	}
	
	/** 通过连接提交事务 */
	private void commit() {
		if( !isAutoCommit() ) { // 注意这里有 感叹号
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 通过连接回滚事务 */
	private void rollback() {
		if( !isAutoCommit() ) { // 注意这里有 感叹号
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 专门定义一个方法，让结果集光标向下移动
	 * @param rs
	 * @return
	 */
	public boolean next( ResultSet rs ) {
		try {
			return rs.next() ;
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
	}

}
