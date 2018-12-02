package kita.example.com.kitkatproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by kita on 2017-01-11.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
