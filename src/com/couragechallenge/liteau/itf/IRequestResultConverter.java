package com.couragechallenge.liteau.itf;

import com.couragechallenge.liteau.bean.MapRequestResult;


/**
 * the request result Converter which convert a CharSequence object(data form like json,xml or others) to MapListResult object 
 * @author weisir
 * 2015-4-21
 * @param <T>
 */
public interface IRequestResultConverter extends IConvert<CharSequence,MapRequestResult> {
	
}