
package com.k2mediawebdesign;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blahti.example.drag2.DragController;
import com.blahti.example.drag2.DragLayer;
import com.blahti.example.drag2.DragSource;


public class ChartEdit extends Activity 
implements View.OnLongClickListener, View.OnClickListener,
DragController.DragListener
{
	
	private NotesDbAdapter mDbHelper;
    private Long mRowId;
	
    private static final int CLEAR_ID = Menu.FIRST;
    private static final int HELP_ID = Menu.FIRST+1;
    // Variables

    private DragController mDragController;   // Object that sends out drag-drop events while a view is being moved.
    private DragLayer mDragLayer;             // The ViewGroup that supports drag-drop.

    private ArrayList<PainStickerView> markers = new ArrayList<PainStickerView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.body_chart);
	    mDragController = new DragController(this);
	    setupViews();
	    
	    mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                                    : null;
        }

        populateStickers();
	}

	/**
	 * Handle a click on a view.
	 *
	 */    

	public void onClick(View v) 
	{
		PainStickerView psv = (PainStickerView)v;
		psv.cycleColors();
		
	    //toast ("You clicked. Try a long click");
	}

	/**
	 * Handle a long click.
	 *
	 * @param v View
	 * @return boolean - true indicates that the event was handled
	 */    

	public boolean onLongClick(View v) 
	{
	   // trace ("onLongClick in view: " + v);

	    // Make sure the drag was started by a long press as opposed to a long click.
	    // (Note: I got this from the Workspace object in the Android Launcher code. 
	    //  I think it is here to ensure that the device is still in touch mode as we start the drag operation.)
	    if (!v.isInTouchMode()) {
	       toast ("isInTouchMode returned false. Try touching the view again.");
	       return false;
	    }
	    return startDrag (v);
	}
	
	public void toast (String msg)
	{
	    Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	} // end toast

	/**
	 * Send a message to the debug log and display it using Toast.
	 */

	public void trace (String msg) 
	{
//	    if (!Debugging) return;
//	    Log.d ("DragActivity", msg);
//	    toast (msg);
	}

	/**
	 * Start dragging a view.
	 *
	 */    

	public boolean startDrag (View v)
	{
	    // Let the DragController initiate a drag-drop sequence.
	    // I use the dragInfo to pass along the object being dragged.
	    // I'm not sure how the Launcher designers do this.
	    Object dragInfo = v;
	    mDragController.startDrag (v, mDragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
	    return true;
	}

	/**
	 * Finds all the views we need and configure them to send click events to the activity.
	 *
	 */
	private void setupViews() 
	{

	    DragController dragController = mDragController;
	    dragController.setDragListener(this);
	    mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
	    mDragLayer.setDragController(dragController);
	    dragController.addDropTarget (mDragLayer);

	    // Give the user a little guidance.
	    Toast.makeText (getApplicationContext(), "Record your pain symptoms here.",
	                    Toast.LENGTH_LONG).show ();

	}

	public void click_pain_type(View v) {

		PainStickerView tv = new PainStickerView(this);
		Button b = (Button)v;
		
	    tv.setPainType(tv.getPainTypeIndex(b.getText().toString()));
	    
	    tv.setOnLongClickListener(this);
	    tv.setOnClickListener(this);
	    mDragLayer.addView(tv);
	    markers.add(tv);
	}
	
  private void showToastMessage(String msg){

      Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
      toast.show();

  }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, CLEAR_ID, 0, R.string.menu_clear);
        menu.add(0, HELP_ID, 0, "Help");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case CLEAR_ID:
            	for(PainStickerView psv : markers){
            		mDragLayer.removeView(psv);
            	}
            	markers.clear();
            	mDbHelper.deleteAllStickers(mRowId);
                return true;
                
            case HELP_ID:
            	new AlertDialog.Builder(this)
            	  .setTitle("Help Instructions").setMessage(R.string.help_message)
            	  .setPositiveButton("Ok Thanks",
            	   new DialogInterface.OnClickListener() {
            	    
            	    public void onClick(DialogInterface dialog, int which) {
            	     // TODO Auto-generated method stub
            	     
            	    }
            	   }
            	    )
            	  .show();
            	return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    Object currView = null;
	public void onDragStart(DragSource source, Object info, int dragAction) {
		// TODO Auto-generated method stub
		currView = info;
	}

	public void onDragEnd(View v, int dropTop, int dropLeft) {
		if(currView != null) {

			View globalView = findViewById(R.id.linearLayout1);
        	DisplayMetrics dm = new DisplayMetrics();
        	this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        	int topOffset = dm.heightPixels - globalView.getMeasuredHeight();
        	int leftOffset = dm.widthPixels - globalView.getMeasuredWidth();

        	
			PainStickerView psv = (PainStickerView)currView;
			psv.setLocation(dropTop - topOffset, dropLeft - leftOffset/2);

			
			//toast(String.valueOf(dropLeft - leftOffset/2) + " " + String.valueOf(dropTop- topOffset));
			currView = null;
		}
		
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateStickers();
    }
    
    private void populateStickers()
    {
    	if(markers.size() > 0) {return;}
    	markers.clear();
    	Cursor stickersCursor = mDbHelper.fetchAllStickers(mRowId);
        startManagingCursor(stickersCursor);
        
        stickersCursor.moveToFirst();
		while (stickersCursor.isAfterLast() == false) {
		    
			PainStickerView tv = new PainStickerView(this);
			String x_str = stickersCursor.getString(stickersCursor.getColumnIndexOrThrow("x"));
			String y_str = stickersCursor.getString(stickersCursor.getColumnIndexOrThrow("y"));
			String type_str = stickersCursor.getString(stickersCursor.getColumnIndexOrThrow("pain_type"));
			String intensity_str = stickersCursor.getString(stickersCursor.getColumnIndexOrThrow("intensity"));
			
		    tv.setPainType(Integer.parseInt(type_str));
		    tv.setIntensity(Integer.parseInt(intensity_str));
		    tv.setOnLongClickListener(this);
		    tv.setOnClickListener(this);
		    
		    mDragLayer.addView(tv);
		    markers.add(tv);
		    int w = -2;
		    int h = -2;
		    int left = (int)Math.round(Float.parseFloat(x_str));
		    int top = (int)Math.round(Float.parseFloat(y_str));
		    tv.setLocation(top, left);
		    
		    DragLayer.LayoutParams lp = new DragLayer.LayoutParams(w, h, left, top);
		    mDragLayer.updateViewLayout(tv, lp);
		    stickersCursor.moveToNext();
		}
		
    }
    
    
    private void saveState() {

        if (mRowId != null) {
        	mDbHelper.deleteAllStickers(mRowId);
        	for(PainStickerView psv : markers){
        		PainSticker model = psv.getModel();
        		mDbHelper.createSticker(mRowId, model.x, model.y, model.type, model.intensity);
        	}
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDbHelper != null) {
        	mDbHelper.close();
        }
    }

	
}
