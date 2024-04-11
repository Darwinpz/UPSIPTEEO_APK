package com.dpilaloa.upsipteeo.Controladores;

import android.app.AlertDialog;

import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;

public class Interfaces {

    public Interfaces(){}

    public interface build{
        void verbuilder(AlertDialog.Builder builder);
    }

    /**
     * @implNote Obtener el Objeto de tipo Usuario
     */
    public interface Firebase_calluser
    {
        void datos_usuario(Ob_usuario user);
    }

    /**
     * @implNote Obtener un contador de registros
     */
    public interface Firebase_count{
        void count(long cantidad);
    }

}

