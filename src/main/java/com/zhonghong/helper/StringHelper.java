package com.zhonghong.helper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 这是一个用来操作字符串的工具类
 */
public final class StringHelper {
	
	public static void main(String[] args) {
		System.out.println( StringHelper.random( 10 ) );
	}

	private static Random random = new SecureRandom();

	/**
	 * 随机产生一个含有 n 个字符的字符串( 这些字符可以是 a ~ z 、A ~ Z、0 ~ 1  之间的任意字符 )
	 * @param n 随机产生的字符串的字符个数
	 * @return 返回一个含有n个字符的字符串
	 */
	public static String random( final int n ){
		final char begin = 'a' ;
		final char end = 'z' ;
		final int length = end - begin ;
		StringBuilder buffer =  new StringBuilder();

		for( int i = 0 ; i < n ; i ++ ){
			int offset = random.nextInt( length);
			char ch = (char) ( begin + offset );
			buffer.append( ch );
		}

		return buffer.toString();
	}
	
	/**
	 * 判断一个字符串是否为 null 或 空串 或 空白
	 * @param source 需要判断的字符串
	 * @return 当字符串为 null 或 为 空白、空串 时返回 true
	 */
	public static final boolean empty( String source ) {
		return source == null || source.trim().isEmpty()  ;
	}
	
	/**
	 * 判断一个字符串是否不是null且不是空串、不是空白
	 * @param source 需要判断的字符串
	 * @return 当 字符串是不是null且不是空串也不是空白时返回 true
	 */
	public static final boolean notEmpty( String source ) {
		return source != null && source.trim().length() > 0 ;
	}
	
	/**
	 * 判断一个字符串变量是否为 null
	 * @param source 需要判断的字符串
	 * @return 当 字符串变量 为 null 时返回 true
	 */
	public static final boolean isNull( String source ) {
		return source == null ;
	}
	
	/**
	 * 判断一个字符串是否为 空串
	 * @param source 需要判断的字符串
	 * @return 当字符串中的值是 空串 或 空白 串时返回 true
	 */
	public static final boolean emptyString( String source ) {
		return ( source != null ) && source.length() == source.trim().length() ;
	}
	
	/**
	 * 判断一个字符串是否为 空白 串
	 * @param source 需要判断的字符串
	 * @return 当字符串中的值是 空白 串时返回 true
	 */
	public static final boolean blank( String source ){
		return ( source != null ) && source.length() > source.trim().length() && source.trim().isEmpty() ;
	}
	
	/**
	 * 比较两个非空(不是null，不是空串、不是空白)字符串是否"相等"
	 * @param one 第一个需要比较的字符串
	 * @param theOther 另一个参与比较的字符串
	 * @return 当 两个字符串 都不为空串 且 内容完全一致 (剔除首尾空白后、大小写也一致)时返回 true
	 */
	public static final boolean equals( String one , String theOther) {
		return equals(one, theOther,true,false);
	}
	
	/**
	 * 比较两个字符串是否 "相等"
	 * @param one 参与比较的第一个字符串
	 * @param theOther 参与比较的另一个字符串
	 * @param escapeSpace 是否需要剔除首尾空白 ( true 表示需要剔除首尾空白，false 表示不剔除 )
	 * @param ignoreCase 是否忽略大小写 ( true 表示忽略大小写 ，false 表示不忽略大小写 )
	 * @return
	 */
	public static final boolean equals( String one , String theOther , boolean escapeSpace , boolean ignoreCase) {
		
		if( one == null || theOther == null ){
			return false ;
		}
		
		if( escapeSpace ){
			one = one.trim();
			theOther = theOther.trim();
		}
		
		return ignoreCase ? one.equalsIgnoreCase( theOther ) : one.equals( theOther ) ;
	}
	
	public static final String uuid() {
		// 随机产生一个 UUID 实例
		UUID uuid = UUID.randomUUID(); 
		// 获得 UUID 实例中的字符串 ( 是一个包含了 4 个 - 好的36位长度的字符串 )
		String uuidString = uuid.toString() ; 
		// 剔除 字符串中的 - 字符 (用空串替换)
		uuidString = uuidString.replace( "-", "" );
		// 将字符串全部转换成大写字母
		uuidString = uuidString.toLowerCase() ;
		return uuidString ;
	}
	
	

	/**
	 * 将字符序列中的所有字符全部转大写
	 * @param cs 需要转换的字符序列，可以是 String 、StringBuffer 、StringBuilder 等
	 * @return 返回转换为大写后的字符串
	 */
	public static final String upperCase( CharSequence cs ){
		
		String result = "" ;
		
		if( cs != null ) {
			
			Class<?> type = cs.getClass() ;
		
			if( type == String.class ){
				String s = (String) cs ;
				result = s.toUpperCase();
			} else if( type == StringBuffer.class ){
				StringBuffer buffer = (StringBuffer) cs;
				for( int i = 0 , n = buffer.length() ; i < n ; i++ ){
					char ch = buffer.charAt( i );
					if( isLowerCase( ch ) ) {
						buffer.deleteCharAt( i );
						buffer.insert( i ,  (char) ( ch - 32 ) );
					}
				}
				result = buffer.toString();
			} else if( type == StringBuilder.class ){
				StringBuilder builder = (StringBuilder) cs;
				for( int i = 0 , n = builder.length() ; i < n ; i++ ){
					char ch = builder.charAt( i );
					if( isLowerCase( ch ) ) {
						builder.deleteCharAt( i );
						builder.insert( i ,  (char) ( ch - 32 ) );
					}
				}
				result = builder.toString();
			} 
		}
		
		return result ;

	}
	
	/**
	 * 判断指定字符是否是小写字符
	 * @param c 需要判断的字符
	 * @return 如果被判断的字符是小写字符就返回 true
	 */
	public static final boolean isLowerCase( char c ) {
		if( c >= 'a' && c <= 'z' ){
			return true ;
		} else {
			return false ;
		}
	}
	
}
