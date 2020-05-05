package com.ksu.serene.controller.liveChart;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksu.serene.controller.liveChart.animation.AnimationManager;
import com.ksu.serene.controller.liveChart.animation.data.AnimationValue;
import com.ksu.serene.controller.liveChart.draw.DrawManager;
import com.ksu.serene.controller.liveChart.draw.data.Chart;


public class ChartManager implements AnimationManager.AnimationListener {

	private DrawManager drawManager;
	private AnimationManager animationManager;
	private AnimationListener listener;

	public interface AnimationListener {

		void onAnimationUpdated();
	}


	public ChartManager(@NonNull Context context, @Nullable AnimationListener listener) {
		this.drawManager = new DrawManager(context);
		this.animationManager = new AnimationManager(drawManager.chart(), this);
		this.listener = listener;
	}

	public Chart chart() {
		return drawManager.chart();
	}

	public DrawManager drawer() {
		return drawManager;
	}

	public void animate() {
		if (!drawManager.chart().getDrawData().isEmpty()) {
			animationManager.animate();
		}
	}

	@Override
	public void onAnimationUpdated(@NonNull AnimationValue value) {
		drawManager.updateValue(value);
		if (listener != null) {
			listener.onAnimationUpdated();
		}
	}
}
