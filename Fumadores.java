import java.util.Random;
import java.util.concurrent.Semaphore;
/*
    Nosotros Mauricio A. De León Cárdenas y Diego Elizondo Bennet
    damos nuestra palabra que hemos trabajado en este
    laboratorio con integridad académica
*/

class FumadorTabaco extends Thread{
    public void run(){
        try{
            while(true){
                if(Fumadores.cerillo.availablePermits() >= 1 && Fumadores.papel.availablePermits() >= 1){
                    System.out.println("Fumador con tabaco entro");
                    System.out.println("Fumador con tabaco pide papel");
                    Fumadores.papel.acquire();
                    System.out.println("Fumador con tabaco pide cerillos");
                    Fumadores.cerillo.acquire();
                    
                    Random rand = new Random();
                    int randomNum = rand.nextInt(2000);          
                    Thread.sleep(randomNum);//Aqui fuma
                    System.out.println("Fumador con tabaco libera mesa");
                    Fumadores.mesa.release();
                }
            }
        } catch (InterruptedException e){
            System.out.println("Error en thread del Tablas: " + e);
        }
    }
}

class FumadorPapel extends Thread{
    public void run(){
        try{
            while(true){
                if(Fumadores.cerillo.availablePermits() >= 1 && Fumadores.tabaco.availablePermits() >= 1){
                    System.out.println("Fumador con papel entro");
                    System.out.println("Fumador con papel pide cerillos");
                    Fumadores.cerillo.acquire();
                    System.out.println("Fumador con papel pide tabaco");   
                    Fumadores.tabaco.acquire();
                    Random rand = new Random();
                    int randomNum = rand.nextInt(2000);          
                    Thread.sleep(randomNum);//Aqui fuma
                    System.out.println("Fumador con papel libera mesa");
                    Fumadores.mesa.release(); 
                }
            }
        } catch (InterruptedException e){
            System.out.println("Error en thread del Tablas: " + e);
        }
    }
}

class FumadorCerillo extends Thread{
    public void run(){
        try{
            while(true){
                if(Fumadores.papel.availablePermits() >= 1 && Fumadores.tabaco.availablePermits() >= 1){
                    System.out.println("Fumador con cerillos entro");
                    System.out.println("Fumador con cerillos pide papel");
                    Fumadores.papel.acquire();
                    System.out.println("Fumador con cerillos pide tabaco"); 
                    Fumadores.tabaco.acquire();          
                    Random rand = new Random();
                    int randomNum = rand.nextInt(2000);          
                    Thread.sleep(randomNum);//Aqui fuma
                    System.out.println("Fumador con cerillos libera mesa");
                    Fumadores.mesa.release();
                }
            }
        } catch (InterruptedException e){
            System.out.println("Error en thread del Tablas: " + e);
        }
    }
}

class Agente extends Thread{
    //0 = tabaco
    //1 = papel
    //2 = cerillos
    int materiales[] = new int[]{0,1,2};
    static int material1 = 0;
    static int material2 = 0;

    public void run(){
        try{
            while(true){
                while(material1 == material2){
                    material1 = seleccionarRecurso();
                    material2 = seleccionarRecurso();
                }
                System.out.println("Agente pide la mesa");
                Fumadores.mesa.acquire();
                if(material1 == 0){
                    System.out.println("Agente poniendo en la mesa tabaco");
                    Fumadores.tabaco.release();
                } else if (material1 == 1){
                    System.out.println("Agente poniendo en la mesa papel");
                    Fumadores.papel.release();
                } else {
                    System.out.println("Agente poniendo en la mesa cerillos");
                    Fumadores.cerillo.release();
                }

                if(material2 == 0){
                    System.out.println("Agente poniendo en la mesa tabaco");
                    Fumadores.tabaco.release();
                } else if (material2 == 1){
                    System.out.println("Agente poniendo en la mesa papel");
                    Fumadores.papel.release();
                } else {
                    System.out.println("Agente poniendo en la mesa cerillos");
                    Fumadores.cerillo.release();
                }
                Random rand = new Random();
                int randomNum = rand.nextInt(2000);          
                Thread.sleep(randomNum);//Aqui fuma
                material1 = 0;
                material2 = 0;
            }
        } catch (InterruptedException e){
            System.out.println("Error en thread del Tablas: " + e);
        }
    }

    public int seleccionarRecurso(){
        Random rand = new Random();
        int randomNum = rand.nextInt(3);       
        return randomNum;
    }
}




class Fumadores{

    public static Semaphore cerillo = new Semaphore(0);
    public static Semaphore papel = new Semaphore(0);
    public static Semaphore tabaco = new Semaphore(0);
    public static Semaphore mesa = new Semaphore(1);
    public static void main(String args[]) {
        FumadorTabaco t = new FumadorTabaco();
        t.start();
  
        FumadorPapel p = new FumadorPapel();
        p.start();
        
        FumadorCerillo c = new FumadorCerillo();
        c.start();

        Agente a = new Agente();
        a.start();
     }   
}