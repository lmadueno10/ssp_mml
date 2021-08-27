package com.appsereno.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.appsereno.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.ak1.pix.helpers.PixBus;
import io.ak1.pix.helpers.PixEventCallback;
import io.ak1.pix.helpers.SystemUiHelperKt;
import io.ak1.pix.models.Flash;
import io.ak1.pix.models.Mode;
import io.ak1.pix.models.Options;
import io.ak1.pix.models.Ratio;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static io.ak1.pix.helpers.UsabilityHelperKt.addPixToActivity;


public class GalleryAndCameraActvity extends AppCompatActivity {
    @Nullable
    private final String TAG = "PIX_CAMERA";
    @Nullable
    private final Options options;

    public GalleryAndCameraActvity(){
        this.options=initOptions();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_camera_reportar_incidencia);
        SystemUiHelperKt.setupScreen(this);

        ActionBar bar = getSupportActionBar();
        if(bar!=null){
            bar.setTitle("PIXI_IMAGE");
        }
        showCameraFragment();
    }
    private Options initOptions(){
        Options options= new Options();
        options.setRatio(Ratio.RATIO_AUTO);
        options.setCount(10);
        options.setSpanCount(3);
        options.setPath("evidencia");
        options.setVideoDurationLimitInSeconds(30);
        options.setMode(Mode.All);
        options.setFlash(Flash.Auto);
        options.setPreSelectedUrls(new ArrayList<Uri>());
        options.setFrontFacing(false);
        return options;
    }

    private void showCameraFragment() {
        addPixToActivity(GalleryAndCameraActvity.this, R.id.cameraContainer, options, new Function1() {
            @Override
            public Object invoke(Object o) {
                this.invoke((PixEventCallback.Results)o);
                return Unit.INSTANCE;
            }
            public final void invoke(@NotNull PixEventCallback.Results results) {
                switch(results.getStatus()){
                    case SUCCESS:
                        ArrayList<String> lista= new ArrayList<>();
                        for (Uri uri:results.getData()) {
                            lista.add(uri.toString());
                        }
                        Intent i= new Intent();
                        i.putStringArrayListExtra("result",lista);
                        setResult(RESULT_OK,i);
                        finish();
                        break;
                    case BACK_PRESSED:
                        getSupportFragmentManager().popBackStack();
                        break;
                }
            }
            /*
            @Override
            public Unit invoke(PixEventCallback.Results results) {
                switch(results.getStatus()){
                    case SUCCESS:
                        ArrayList<String> lista= new ArrayList<>();
                            for (Uri uri:results.getData()) {
                                lista.add(uri.toString());
                            }
                        Intent i= new Intent();
                            i.putStringArrayListExtra("result",lista);
                            setResult(RESULT_OK,i);
                            finish();
                        break;
                    case BACK_PRESSED:
                        getSupportFragmentManager().popBackStack();
                        break;
                }
                return Unit.INSTANCE;
            }*/
        });
    }
    @Nullable
    public Options getOptions(){
        return this.options;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
