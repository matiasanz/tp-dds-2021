package Utils.Factory;

import Modelo.Hogares.Ubicacion;
import Modelo.Mascotas.Especie;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Mascotas.MascotaRescatada;
import Modelo.Mascotas.SexoMascota;

public class ProveedorDeMascotas {
    public static MascotaIdentificada mascotaIdentificada() {
        return new MascotaIdentificada("Pituso", "Pitu", 9, SexoMascota.MACHO,
            "medio salame", Especie.PERRO, ProveedorDeClientes.getClienteFull()
        );
    }

    public static MascotaRescatada rescatadaStub(){
        return rescatada("parche en el ojo y sombrero pirata");
    }

    public static MascotaRescatada rescatada(String descripcion){
        return new MascotaRescatada(ProveedorDeClientes.getClienteFull(), new Ubicacion("Medrano 951",-34.5985998,-58.4221104),descripcion);
    }
}
