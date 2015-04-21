package com.wechallenge.impl;

import android.database.Cursor;

import com.wechallenge.itf.ICursorProcessor;

/**
 * convert a cursor row of sqlite to a string 
 * @author weisir
 * 2015-4-21
 */
public class CursorStringProcessor implements ICursorProcessor<String> {
	
	public static final CursorStringProcessor CURSOR_STRING_PROCESSOR = new CursorStringProcessor();
	
	private String[] fields;
	private String joinner;
	private volatile int[] fldIdx = null;

	public CursorStringProcessor() {
		this.fields = null;
		this.joinner = ",";
	}

	public CursorStringProcessor(String[] fields, String joinner) {
		this.fields = fields;
		this.joinner = joinner==null?"":",";
	}

	public CursorStringProcessor(int[] fieldIndex, String joinner) {
		fldIdx = fieldIndex;
		this.joinner = joinner==null?"":",";
	}

	@Override
	public String convert(Cursor c) {
			if(null == fldIdx) {
				synchronized (this.joinner) {
					if(null == fldIdx){
						initFldIdx(c);
					}
				}
			}

		StringBuilder builder = new StringBuilder();
		for (int i = 0, n = fldIdx.length; i < n; i++) {
			builder.append(c.getString(fldIdx[i]));
			if(i < n - 1) {
				builder.append(joinner);
			}
		}

		return builder.toString();
	}

	private void initFldIdx(Cursor c) {
		if(null == fields) {
			fldIdx = new int[c.getColumnCount()];
			for (int i = 0, n = c.getColumnCount(); i < n; i++) {
				fldIdx[i] = i;
			}
		} else {
			fldIdx = new int[fields.length];
			for (int i = 0, n = fields.length; i < n; i++) {
				fldIdx[i] = c.getColumnIndex(fields[i]);
			}
		}
	}

}
