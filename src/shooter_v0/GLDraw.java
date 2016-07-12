package shooter_v0;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;

import shooter_v0.helper_parent.DebugClass;
import shooter_v0.objects.Camera;
import shooter_v0.objects.Model;
import shooter_v0.objects.Obj;
import shooter_v0.objects.Player;
import shooter_v0.objects.Point3d;
import shooter_v0.objects.Polygon;

public class GLDraw extends DebugClass {
	
	static final private float VIEW_ANGLE_KOEF=90;
	static final private float FAR_BORDER=2000;
	static final private float NEAR_BORDER=1;
	
	static private GL2 gl2;
	static private GLCanvas glcanvas;
	private Engine parentEngine;
	void dispose()
	{
		glcanvas.dispose();
	}

	public	GLDraw(Engine parentEngine)
	{
		this.parentEngine=parentEngine;
	}
	
	GLCanvas init(Composite composite)
	{
		int width=composite.getClientArea().width;
		int height=composite.getClientArea().height;
		GLData gldata = new GLData();
		gldata.doubleBuffer = true;
		glcanvas = new GLCanvas( composite, SWT.NO_BACKGROUND, gldata);
		glcanvas.setSize(width, height);
		glcanvas.setCurrent();
		final GLContext glcontext = GLDrawableFactory.getFactory( GLProfile.getDefault() ).createExternalGLContext();
		glcanvas.setCurrent();
		glcontext.makeCurrent();
		gl2=glcontext.getGL().getGL2();
		gl2.glClearColor(0.7f, 0.7f, 1.0f, 0.0f);
		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glEnable( GL2.GL_NORMALIZE );
	    gl2.glEnable( GL2.GL_LIGHTING );  
	    gl2.glEnable( GL2.GL_LIGHT0 );  
	    gl2.glEnable( GL2.GL_LIGHT1);
		gl2.glMatrixMode( GL2.GL_PROJECTION );
		gl2.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(VIEW_ANGLE_KOEF, (float)(width/height), NEAR_BORDER, FAR_BORDER);
		gl2.glMatrixMode( GL2.GL_MODELVIEW );
	    gl2.glLoadIdentity();
	    gl2.glViewport( 0, 0, width, height );
	      
	    
	    
        gl2.glLightModelf(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
		return glcanvas;
		
	}
    
	void drawModel(Obj obj){
		gl2.glPushMatrix();
		FloatBuffer color=FloatBuffer.wrap(new float[] { (float) (obj.model.color.getRed()/255.0),  (float) (obj.model.color.getGreen()/255.0), (float) (obj.model.color.getBlue()/255.0), 0f}); 	
		gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, color); 
		gl2.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, color);
		gl2.glTranslated(obj.x, obj.y, obj.z);
		if(obj.scaleX!=0)
		{
			gl2.glScaled(obj.scaleX, obj.scaleY, obj.scaleZ);
		}
		gl2.glRotated(-obj.orientation, 0, 0, 1);
    	
		
		int polygonIndex=0;
		Polygon plgBuf;
		Point3d pointBuf;
        while (polygonIndex<obj.model.polygons.size())
        {
        	plgBuf=obj.model.polygons.get(polygonIndex);
        	int pointIndex=0;
        	gl2.glBegin(GL2.GL_POLYGON);
        	gl2.glNormal3f((float)plgBuf.norm.x,(float)plgBuf.norm.y,(float)plgBuf.norm.z);
        	while (pointIndex<plgBuf.v.size()){
        		pointBuf=plgBuf.v.get(pointIndex);
        		gl2.glVertex3f((float)pointBuf.x, (float)pointBuf.y,(float) pointBuf.z);
        		pointIndex++;
        	}
        	gl2.glEnd();
        	polygonIndex++;
        }
        gl2.glPopMatrix();
	}
	
    void draw(ArrayList<Obj> world, Camera cam)
    {
    	//System.out.println("IN DRAW   "+world.size());
    	gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        gl2.glLoadIdentity();    
    	gl2.glRotated(-90, 1, 0, 0);
    	gl2.glRotated( cam.orientation, 0, 0, 1);
        gl2.glTranslated(-cam.x, -cam.y, -cam.z);
        float[] lightPos0 = { 150,100,-200,1 };        // light position
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,lightPos0, 0);
        float[] lightPos1 ={ -200,-300,-1000,1 };
        gl2.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION,lightPos1, 0);

        int modelIndex=0;
        while (modelIndex<world.size())
        {
        	Obj buf=world.get(modelIndex);
        	buf=Obj.parseObj(buf, parentEngine.game.models);
        	drawModel(buf);
        	modelIndex++;
        }
        glcanvas.swapBuffers();
    }


}
