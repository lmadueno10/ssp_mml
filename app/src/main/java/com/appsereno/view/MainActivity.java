package com.appsereno.view;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.appsereno.R;
import com.appsereno.config.GlobalVariables;
import com.appsereno.helper.NotificacionHelper;
import com.appsereno.view.fragments.MainFragment;
import com.appsereno.view.fragments.MisDatosFragment;
import com.appsereno.view.fragments.PendientesFragment;
import com.appsereno.view.fragments.ReportarIncidenciaFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * MainActivity is the class that is bound to the activity_main.xml view
 */
public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;
    private Socket socket;
    private Integer idPersonal;
    private Integer pendientes=0;

    NotificationManagerCompat notiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notiManager=NotificationManagerCompat.from(this);
        materialToolbar=findViewById(R.id.app_main_menu);
        setSupportActionBar(materialToolbar);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,materialToolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container,new MainFragment());
        fragmentTransaction.commit();
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String userInfo = preferences.getString("user_info", "-");
        idPersonal=preferences.getInt("id_personal",0);
        fillData();
        fillMenu();
        loadBottomMenu();
        socket = getIntent().getParcelableExtra("socket");
        if (socket==null){
            initSocket();
        }
        connectSocket(userInfo);
    }

    public void logout(MenuItem item) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder= new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Cerrar sesión");
        materialAlertDialogBuilder.setMessage("¿Estas seguro que deseas cerrar tu sesión?");
        materialAlertDialogBuilder.setPositiveButton("Cerrar sesión", (dialog, which) -> {
            SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("auth_token",null);
            editor.putString("refresh_token",null);
            editor.apply();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        });
        materialAlertDialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> {

        });
        materialAlertDialogBuilder.show();
    }

    private  void fillData(){
        try {
            SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            String userInfo = preferences.getString("user_info", "-");
            String userName = preferences.getString("user", "-");
            View headerView = navigationView.getHeaderView(0);
            if (headerView != null) {
                TextView title = headerView.findViewById(R.id.dwh_text_main);
                TextView subTutle = headerView.findViewById(R.id.dwh_text_secondary);
                if (title != null && subTutle != null) {
                    title.setText(userInfo);
                    subTutle.setText(userName);
                }
            }
        }catch (Exception e){
            Log.d("ERROR:",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("new message");
    }

    private void fillMenu() {
        try {

            SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            int userProfile = preferences.getInt("user_profile", 0);
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.getItem(0);
            SubMenu subMenu = menuItem.getSubMenu();

            MenuItem menuItemMD = subMenu.getItem(0);
            menuItemMD.setOnMenuItemClickListener(item -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new MisDatosFragment());
                fragmentTransaction.commit();
                materialToolbar.setTitle("Mis datos");
                return true;
            });

            MenuItem menuItemMT = subMenu.add("Notificaciones");
            menuItemMT.setIcon(R.drawable.ic_notifications_24);

            if (userProfile != 1) {
                MenuItem menuItemLS = subMenu.add("Listado de serenos");
                menuItemLS.setIcon(R.drawable.ic_supervised_user_circle_24);
            }

            MenuItem menuItemAD = subMenu.add("Acerca del APP");
            menuItemAD.setIcon(R.drawable.ic_info_24);

            MenuItem menuItemTC = subMenu.add("Términos y condiciones");
            menuItemTC.setIcon(R.drawable.ic_book_24);
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
    public void loadBottomMenu(){
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_pending);
        badge.setVisible(true);
        badge.setNumber(0);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id=item.getItemId();
        final int pageHome=R.id.page_home;
        final int pagePending = R.id.page_pending;
        final int pageReport=R.id.page_report;
        final int pageStreaming=R.id.page_stream;
        switch (id){
            case pageHome:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new MainFragment());
                fragmentTransaction.commit();
                materialToolbar.setTitle("App Sereno");
                return true;
            case pagePending:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new PendientesFragment());
                fragmentTransaction.commit();
                materialToolbar.setTitle("Pendientes");
                return true;
            case pageReport:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new ReportarIncidenciaFragment());
                fragmentTransaction.commit();
                materialToolbar.setTitle("Reportar Incidencia");
                return true;
            case pageStreaming :
                /*
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new TransmitirFragment());
                fragmentTransaction.commit();
                materialToolbar.setTitle("Transmitir video");*/
                initStreaming();
        }
        return false;
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void initCamera(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(takeVideoIntent);
        }
    }

    public void initStreaming(){
        Intent intent= new Intent(this,StreamingActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_bar_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        final int id= menuItem.getItemId();
        final int topItemStream=R.id.top_item_stream;
        //final int topItemSearch=R.id.top_item_search;
        final int topItemLogout=R.id.top_item_logout;
        switch(id){
            case topItemStream:
                initStreaming();
                //initCamera();
                break;
            //case topItemSearch:
              //  Snackbar.make(findViewById(R.id.main_container),"Buscando...",Snackbar.LENGTH_LONG).show();
                //break;
            case topItemLogout:
                logout(menuItem);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initSocket(){
        try {
           socket = IO.socket(GlobalVariables.getSocketUri());

           socket.on("usuario_" + idPersonal, new Emitter.Listener() {
               @Override
               public void call(Object... args) {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               updateBadge(args);
                               notificar(args);
                           }catch (JSONException jo){
                               Log.e("SOCKET",jo.getMessage());
                           }
                       }
                   });

               }
           });
           socket.on("login_" + idPersonal, new Emitter.Listener() {
               @Override
               public void call(Object... args) {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           JSONObject obj= (JSONObject) args[0];
                           if(obj!=null){
                               try {
                                   pendientes=obj.getInt("asig")+obj.getInt("repo");
                                   BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_pending);
                                   badge.setVisible(true);
                                   badge.setNumber(pendientes);

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                           Log.i("SOCKET",obj.toString());
                       }
                   });
               }
           });
           socket.on("pendientes_" + idPersonal, new Emitter.Listener() {
               @Override
               public void call(Object... args) {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           JSONObject obj= (JSONObject) args[0];
                           if(obj!=null){
                               try {
                                   pendientes=obj.getInt("asig")+obj.getInt("repo");
                                   BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_pending);
                                   badge.setVisible(true);
                                   badge.setNumber(pendientes);

                                   TabLayout tabLayout=findViewById(R.id.tabLayout);
                                   if(tabLayout!=null){
                                       TabLayout.Tab tb= tabLayout.getTabAt(0);
                                       BadgeDrawable bd=tb.getBadge();
                                       bd.setNumber(obj.getInt("asig"));
                                       TabLayout.Tab tb1= tabLayout.getTabAt(1);
                                       BadgeDrawable bd1=tb1.getBadge();
                                       bd1.setNumber(obj.getInt("repo"));
                                   }

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   });
               }
           });
        }catch (Exception e) {
            Toast.makeText(this,"Ocurrio un erro al conectar al servidor",Toast.LENGTH_LONG).show();
            Log.e("SOCKET_ERROR",e.getMessage());
        }
    }

    public void updateBadge(Object... obj) throws JSONException {
        if(obj[0]!=null){
            JSONObject data= (JSONObject) obj[0];
            JSONObject count=data.getJSONObject("count");

            pendientes=(count.getInt("asig")+count.getInt("repo"));
            BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_pending);
            badge.setVisible(true);
            badge.setNumber(pendientes);
        }
    }

    private void showToast(Object... o){
        pendientes++;
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_pending);
        badge.setVisible(true);
        badge.setNumber(pendientes);
        if(o!=null)
            Log.i("SOCKET",o[0].toString());
    }
    public void connectSocket(String userName){
        JSONObject user= new JSONObject();
        try {
            user.put("userName",userName);
            user.put("id_personal",idPersonal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.connect();
        socket.emit("login",user);
        Log.i("SOCKET_LOGIN",user+" Conectado");
    }
    public Socket getSocket(){
        return this.socket;
    }

    public void notificar(Object... obj) throws JSONException {
        if(obj[0]!=null){
            JSONObject data = (JSONObject) obj[0];
            JSONObject in=data.getJSONObject("incidencia");
            String description=in.getString("descripcion");
            String direccion=in.getString("direccion");
            Log.i("SOCKET",in.toString());
            Intent intent = new Intent(this,MainActivity.class);
            PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
            NotificacionHelper.createChanelNotification(this);
            NotificationCompat.Builder builder= new NotificationCompat.Builder(this, NotificacionHelper.ID_CHANEL_1)
                    .setSmallIcon(R.drawable.ic_local_police_24)
                    .setContentTitle(direccion)
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.GREEN)
                    .setContentIntent(pi)
                    .setAutoCancel(true);


            notiManager.notify(0,builder.build());
        }
    }
}