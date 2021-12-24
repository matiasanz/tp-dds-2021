package Modelo.Usuarios.Validaciones;

import Utils.Resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ValidacionNoTrivial extends ValidacionContrasenia{

    @Override
    public String getMensaje() {
        return "La contrasenia es demasiado trivial";
    }

    @Override
    public boolean contraseniaValida(String contrasenia){
        return !contraseniaTrivial(contrasenia);
    }

    public boolean contraseniaTrivial(String contrasenia) {
        File file = Resources.getFile("Miscelaneas/10000WorstPasswords.txt");
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(contrasenia)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
            return false;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
