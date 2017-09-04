package randomcompany.estimote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.estimote.coresdk.cloud.api.CloudCallback;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.exception.EstimoteCloudException;
import com.estimote.indoorsdk.algorithm.IndoorLocationManagerBuilder;
import com.estimote.indoorsdk.algorithm.OnPositionUpdateListener;
import com.estimote.indoorsdk.algorithm.ScanningIndoorLocationManager;
import com.estimote.indoorsdk.cloud.IndoorCloudManager;
import com.estimote.indoorsdk.cloud.IndoorCloudManagerFactory;
import com.estimote.indoorsdk.cloud.Location;
import com.estimote.indoorsdk.cloud.LocationPosition;
import com.estimote.indoorsdk.view.IndoorLocationView;

public class MainActivity extends AppCompatActivity {

    IndoorLocationView indoorLocationView;

    IndoorCloudManager cloudManager;

    ScanningIndoorLocationManager indoorLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EstimoteSDK.initialize(getApplicationContext(), "locationbuilder-project-hd6", "faae6c28864872153523cdb072e4cc70");
        EstimoteSDK.enableDebugLogging(true);

        cloudManager = new IndoorCloudManagerFactory().create(this);
        cloudManager.getLocation("otthon-", new CloudCallback<Location>() {
            @Override
            public void success(Location location) {

                indoorLocationView = (IndoorLocationView) findViewById(R.id.indoor_view);
                indoorLocationView.setLocation(location);

                indoorLocationManager =
                        new IndoorLocationManagerBuilder(getApplicationContext(), location)
                                .withDefaultScanner()
                                .build();

                indoorLocationManager.startPositioning();

                indoorLocationManager.setOnPositionUpdateListener(new OnPositionUpdateListener() {
                    @Override
                    public void onPositionUpdate(LocationPosition locationPosition) {
                        indoorLocationView.updatePosition(locationPosition);
                    }

                    @Override
                    public void onPositionOutsideLocation() {
                        indoorLocationView.hidePosition();
                    }
                });


            }

            @Override
            public void failure(EstimoteCloudException e) {
                // oops!
                if (1==1)  {
                    Log.e("asdasasd",e.getMessage(),e);

                }
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (indoorLocationManager != null) {


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        indoorLocationManager.stopPositioning();
    }


}
