package com.example.kt.rocketJumpMan;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this));
    }

//    @Override
//    public boolean onCreateOptionsMenu (Menu menu){
//        getMenuInflater().inflate(R.menu.menu_game, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item){
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings)
//            return true;
//
//        return super.onCreateOptionsMenu(item);
//    }
}
