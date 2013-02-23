package com.k2mediawebdesign;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * TabActivity that launches chart edit and note edit activities.
 * @author kwan
 *
 */
public class HealthEntryActivity extends TabActivity  {
	
	private Long mRowId;
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) 
        {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                                    : null;
        }
	    setContentView(R.layout.healthentry_tabs);

	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    
	    // If you have your tab layout in a different file, you have to inflate the XML.
	    //LayoutInflater.from(this).inflate(R.layout.body_chart, tabHost.getTabContentView(), true);
	    
	    Intent i = new Intent(this, ChartEdit.class);
	    i.putExtra(NotesDbAdapter.KEY_ROWID, mRowId);
	    

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("chart").setIndicator("Chart")
	                      .setContent(i);
	    
	    tabHost.addTab(spec);
	    
	    i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, mRowId);
	    spec = tabHost.newTabSpec("note").setIndicator("Note")
                .setContent(i);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	    Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
	    tabHost.getTabWidget().getChildAt(0).setLayoutParams(new
                LinearLayout.LayoutParams((width/2)-2,50));
	    tabHost.getTabWidget().getChildAt(1).setLayoutParams(new
                LinearLayout.LayoutParams((width/2)-2,50));
	}
	
	
}
