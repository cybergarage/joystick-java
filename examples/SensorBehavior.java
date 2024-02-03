/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : SensorBehavior.java
*
******************************************************************/

import javax.media.j3d.*;
import java.util.*;

public class SensorBehavior extends Behavior {

	private WakeupOnElapsedFrames conditions = new WakeupOnElapsedFrames(0);
	private TransformGroup transformGroup;
	private Sensor sensor;
	private Transform3D transform = new Transform3D();

	public SensorBehavior( TransformGroup tg, Sensor sensor ) {
		transformGroup = tg;
		this.sensor = sensor;
	}

	public void initialize() {
		wakeupOn(conditions);
	}

	public void processStimulus(Enumeration criteria) {
		sensor.getRead(transform);
		transformGroup.setTransform(transform);
		wakeupOn(conditions);
	}
}
