package com.ksu.serene.controller.liveChart.draw;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.ksu.serene.controller.liveChart.animation.data.AnimationValue;
import com.ksu.serene.controller.liveChart.draw.data.Chart;


public class DrawManager {

	private DrawController controller;
	private Chart chart;

	public DrawManager(@NonNull Context context) {
		chart = new Chart();
		controller = new DrawController(context, chart);
	}

	public Chart chart() {
		return chart;
	}

	public void updateTitleWidth() {
		controller.updateTitleWidth();
	}

	public void draw(@NonNull Canvas canvas) {
		controller.draw(canvas);
	}

	public void updateValue(@NonNull AnimationValue value) {
		controller.updateValue(value);
	}
}
