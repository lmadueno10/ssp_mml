package com.appsereno.model.base;

import com.appsereno.view.LoginActivity;
import com.appsereno.view.SeguridadActivity;
import com.appsereno.view.StreamingActivity;
import com.appsereno.view.fragments.DetalleIncidenteAcionesFragment;
import com.appsereno.view.fragments.DetalleIncidenteFragment;
import com.appsereno.view.fragments.IncidentesReportadosPorMIFragment;
import com.appsereno.view.fragments.MainFragment;
import com.appsereno.view.fragments.ReportarIncidenciaFragment;
import com.appsereno.viewmodel.adapter.LoginViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * RetrofitComponent is the Interface that contains injection methods
 */
@Singleton
@Component(modules = RetrofitModule.class)
public interface RetrofitComponent {
    /**
     * This method is responsible of the injection the retrofit modules
     * of the SeguridadActivity
     * @param seguridadActivity {@link SeguridadActivity}
     */
    void inject(SeguridadActivity seguridadActivity);
    /**
     * This method is responsible of the injection the retrofit modules
     * of the LoginActivity
     * @param loginActivity {@link LoginActivity}
     */
    void inject(LoginActivity loginActivity);

    void inject(LoginViewModel loginViewModel);

    /**
     * This method is responsible of the injection the retrofit modules
     * of the LoginActivity
     * @param detalleIncidenteFragment {@link DetalleIncidenteFragment}
     */
    void inject(DetalleIncidenteFragment detalleIncidenteFragment);
    /**
     * This method is responsible of the injection the retrofit modules
     * of the ReportarIncidenciaFragment
     * @param reportarIncidenciaFragment {@link ReportarIncidenciaFragment}
     */
    void inject(ReportarIncidenciaFragment reportarIncidenciaFragment);
    /**
     * This method is responsible of the injection the retrofit modules
     * of the DetalleIncidenteAcionesFragment
     * @param detalleIncidenteAcionesFragment {@link DetalleIncidenteAcionesFragment}
     */
    void inject(DetalleIncidenteAcionesFragment detalleIncidenteAcionesFragment);

    /**
     * This method is responsible of the injection the retrofit modules
     * of the IncidentesReportadosPorMIFragment
     * @param incidentesReportadosPorMIFragment {@link IncidentesReportadosPorMIFragment}
     */
    void inject(IncidentesReportadosPorMIFragment incidentesReportadosPorMIFragment);

    /**
     * This method is responsible of the injection the retrofit modules
     * of the IncidentesReportadosPorMIFragment
     * @param streamingActivity {@link StreamingActivity}
     */
    void inject(StreamingActivity streamingActivity);

    /**
     * This method is responsible of the injection the retrofit modules
     * of the IncidentesReportadosPorMIFragment
     * @param mainFragment {@link MainFragment}
     */
    void inject(MainFragment mainFragment);
}
