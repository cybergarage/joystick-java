/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : Sample.java
*
******************************************************************/

import java.applet.Applet;
import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;

public class Sample extends Applet {

	public BranchGroup createSceneGraph() {
		BranchGroup root = new BranchGroup();
		
		Transform3D cubeTrans3D = new Transform3D();
		cubeTrans3D.setTranslation(new Vector3f(0.0f, 0.0f, -10.0f));
		TransformGroup cubeTrans = new TransformGroup(cubeTrans3D);
		root.addChild(cubeTrans);


		TransformGroup cubeRot = new TransformGroup();
		cubeRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cubeTrans.addChild(cubeRot);
		
		cubeRot.addChild(new ColorCube(1.0));

		Transform3D yAxis = new Transform3D();
		Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 4000, 0, 0, 0, 0, 0);
		RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, cubeRot, yAxis, 0.0f, (float) Math.PI*2.0f);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
		rotator.setSchedulingBounds(bounds);
		cubeRot.addChild(rotator);
		
		return root;
	}

	public Sample() {

		setLayout(new BorderLayout());
		Canvas3D c = new Canvas3D(null);
		add("Center", c);

		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);

		JoystickInputDevice joyDevice = new JoystickInputDevice();
		joyDevice.initialize();
		joyDevice.setSensitivity(0.1f);
		joyDevice.setAngularRate(0.02f);
		u.getViewer().getPhysicalEnvironment().addInputDevice( joyDevice );
			
		TransformGroup viewTrans = u.getViewingPlatform().getViewPlatformTransform();
		Sensor joy1 = joyDevice.getSensor(0);
		SensorBehavior s = new SensorBehavior(viewTrans, joy1);
		s.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), Float.MAX_VALUE));
		scene.addChild(s);
		
		u.addBranchGraph(scene);
	}

	public static void main(String[] args) {
		new MainFrame(new Sample(), 350, 350);
	}
}
