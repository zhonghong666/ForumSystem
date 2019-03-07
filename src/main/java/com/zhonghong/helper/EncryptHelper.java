package com.zhonghong.helper;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 这是一个用来实现加密字符串的工具类
 */
public final class EncryptHelper {

    public static void main(String[] args) {

        String source = "zhonghong" ;
        System.out.println( "加密前: " + source );
        String encrypted = EncryptHelper.encrypt( source ) ; // 使用 MD5 对密码进行加密
        System.out.println( "加密后: " + encrypted );
        System.out.println( "加密后的字符串长度: " + encrypted.length() );

        encrypted = EncryptHelper.encrypt( source , EncryptType.SHA1 );
        System.out.println( "加密后: " + encrypted );
        System.out.println( "加密后的字符串长度: " + encrypted.length() );

    }


    public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    public static final Charset UTF8_CHARSET = Charset.forName( "UTF-8" );
    public static final Charset GBK_CHARSET = Charset.forName( "GBK" );

    protected static final Base64.Encoder ENCODER = Base64.getUrlEncoder();
    protected static final Base64.Decoder DECODER = Base64.getUrlDecoder() ;

    /**
     * 根据指定的算法名称获取对应的 "信息摘要" 对象
     * @param algorithmName 指定的算法名称
     * @return
     */
    public static final MessageDigest digest( String algorithmName ){
        try {
            MessageDigest digest = MessageDigest.getInstance( algorithmName );
            return digest ;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException( "不支持[ " + algorithmName + " ]算法" , e );
        }
    }

    /**
     * 使用 MD5 加密算法对 字符串进行加密 ( 如果被加密的字符串中含有中文，则使用 UTF-8 编码处理 )
     * @param source 需要被加密的字符串
     * @return 返回采用 MD5 加密算法加密后的 新字符串
     */
    public static final String encrypt( String source ) {
        return encrypt(source , EncryptType.MD5 , DEFAULT_CHARSET ) ;
    }

    /**
     * 使用指定的 加密算法 并 采用默认编码 对字符串进行加密处理
     * @param source 需要加密的字符串
     * @param type 需要采用的加密算法
     * @return
     */
    public static final String encrypt( String source ,EncryptType type ){
        return encrypt( source , type , DEFAULT_CHARSET );
    }

    /**
     * 使用指定的 加密算法 对字符串进行加密处理
     * @param source 需要加密的字符串
     * @param type 需要采用的加密算法
     * @param cs 字符编码对象(用来将字符串转换为字节序列) ，如果参数为 null 则表示采用默认编码
     * @return 返回采用指定加密算法加密后的字符串
     */
    public static final String encrypt( String source ,EncryptType type , Charset cs ){
        if( StringHelper.empty( source ) ){
            throw new RuntimeException( "被加密的字符串不能为空" );
        }
        // 获得指定 算法名称 对应的 信息摘要对象
        MessageDigest md = digest( type.getAlgorithmName() ) ;
        // 使用指定的字符编码将字符串转换为字节序列
        byte[] bytes = null ;
        if( cs == null ) {
            bytes = source.getBytes();
        } else {
            bytes = source.getBytes( cs ) ;
        }
        // 使用 信息摘要对象 对字节序列 进行加密处理
        md.update( bytes );
        // 获取加密后的字节序列
        byte[] mdBytes = md.digest();
        // 采用 big-endian 将 mdBytes 中的字节转换成一个非负整数 ( 1 表示非负数 )
        BigInteger bigInt = new BigInteger( 1 , mdBytes );
        // 将 非负整数 转换成 16进制形式字符串
        return bigInt.toString(16) ;
    }

}
