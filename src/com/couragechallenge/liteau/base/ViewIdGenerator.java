package com.couragechallenge.liteau.base;

import java.util.concurrent.atomic.AtomicInteger;

import android.view.View;

/**
 * {@link View#generateViewId()}要求API Level >= 17,而本工具类可兼容所有API Level
 * <p>
 * 自动判断当前API Level,并优先调用{@link View#generateViewId()},即使本工具类与
 * {@link View#generateViewId()} 混用,也能保证生成的Id唯一
 * <p>
 * =============
 * <p>
 * while {@link View#generateViewId()} require API Level >= 17, this tool is
 * compatibe with all API.
 * <p>
 * according to current API Level, it decide weather using system API or not.<br>
 * so you can use {@link ViewIdGenerator#generateViewId()} and
 * {@link View#generateViewId()} in the same time and don't worry about getting
 * same id
 * 
 * @author fantouchx@gmail.com
 * 
 *　代码中生成的view的id请使用本类
 */
public class ViewIdGenerator {
	private static final int INIT = 0x00000100;
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(INIT);

	public static int generateViewId() {
		for (;;) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range
			// under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF)
				newValue = INIT; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				return result;
			}
		}

	}
}
