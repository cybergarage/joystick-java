/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : JoystickInputDevice.java
*
******************************************************************/

import javax.media.j3d.*;
import javax.vecmath.*;

public class JoystickInputDevice implements InputDevice {
	
	private Joystick		joy[]				= new Joystick[2];
	private Sensor			joySensor[]		= new Sensor[2];
	private SensorRead	joySensorRead	= new SensorRead();
	private Transform3D	joyTransform	= new Transform3D();
	private Transform3D	rotTransform	= new Transform3D();
	private float			joyPos[][]		= new float[2][3];
	private float			joyRot[][]		= new float[2][3];
	private Vector3f		joyTransVec		= new Vector3f();
	private float			sensitivity		= 1.0f;
	private float			angularRate		= 1.0f;
	private float			x, y, z;
	
	public JoystickInputDevice() {
		try {
			joy[0] = new Joystick(0);
			joy[1] = new Joystick(1);
		}
		catch (SecurityException se) {}
		catch (UnsatisfiedLinkError ule) {}

		joySensor[0] = new Sensor(this);
		joySensor[1] = new Sensor(this);
		
		setSensitivity(0.1f);
		setAngularRate(0.01f);
	}

	public boolean initialize() {
		for (int i=0; i<2; i++) {
			for (int j=0; j<3; j++) {
				joyPos[i][j]	= 0.0f;
				joyRot[i][j]	= 0.0f;
			}
		}
		return true;
	}

	public void close() {
	}

	public int getProcessingMode() {
		return DEMAND_DRIVEN;
	}

	public int getSensorCount() {
		return 2;
	}

	public Sensor getSensor(int id)  {
		return joySensor[id];
	}

	public void setProcessingMode(int mode) {
	}

	public void pollAndProcessInput(Joystick joy, Sensor joySensor, float joyPos[], float joyRot[]) {
		int buttons = joy.getButtons();
		if ( ((buttons & Joystick.BUTTON1) != 0) &&  ((buttons & Joystick.BUTTON2) != 0)) {
			setNominalPositionAndOrientation();
			return;
		}
		
		joySensorRead.setTime(System.currentTimeMillis());
		
		x = joy.getXPos(); 
		z = joy.getYPos();
		
		if (Math.abs(x) < 0.2f)
			x = 0.0f;
		if (Math.abs(z) < 0.2f)
			z = 0.0f;
		
 		joyRot[1] -= x * angularRate;
 		joyPos[2] += z * sensitivity;
 		
 		joyTransVec.x = joyPos[0];
 		joyTransVec.y = joyPos[1];
 		joyTransVec.z = joyPos[2];

 		rotTransform.setIdentity();
 		rotTransform.rotY(joyRot[1]);
 		
 		joyTransform.setIdentity();
		joyTransform.set(joyTransVec);
		joyTransform.mul(rotTransform);

		//System.out.println("POS = " + x + ", " + z);
		//System.out.println("POS = " + joyPos[0] + ", " + joyPos[1] + ", " + joyPos[2]);
		
		joySensorRead.set( joyTransform );
		joySensor.setNextSensorRead( joySensorRead );
	}
	
	public void pollAndProcessInput() {
		pollAndProcessInput(joy[0], joySensor[0], joyPos[0], joyRot[0]);
		pollAndProcessInput(joy[1], joySensor[1], joyPos[1], joyRot[1]);
	}

	public void processStreamInput() {
	}

	public void setNominalPositionAndOrientation() {
		initialize();
		joySensorRead.setTime(System.currentTimeMillis());
		joyTransform.setIdentity();
		joySensorRead.set(joyTransform);
		joySensor[0].setNextSensorRead(joySensorRead);
		joySensor[1].setNextSensorRead(joySensorRead);
	}
	
	public void setSensitivity(float value) {
		sensitivity = value;
	}

	public float getSensitivity() {
		return sensitivity;
	}
	
	public void setAngularRate(float value) {
		angularRate = value;
	}

	public float getAngularRate() {
		return angularRate;
	}
}
