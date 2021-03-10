package com.example.clientealgoritmodecristian;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cliente extends Thread{
    private String server;
    private int port;
    private long t0;
    private long t3;
    private long tcliente;
    private long t1;
    private long t2;

    public Cliente(String server, int port) {
        this.server = server;
        this.port = port;
    }
    void connection(){
        try {
            System.out.println("Conectando a ... " + server + " na porta " + port);

            Socket cliente = new Socket(server, port);
            System.out.println("Conectado a " + cliente.getRemoteSocketAddress());

            t0 = System.currentTimeMillis() - random();
            OutputStream outToServer = cliente.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeLong(t0);

            InputStream inFromServer = cliente.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            tcliente = in.readLong();   //Recebe o tempo do proprio cliente
            t1 = in.readLong();   //Recebe o tempo real do servidor
            t2 = in.readLong();   //Recebe o tempo de resposta do servidor
            t3 = System.currentTimeMillis();
            t2-=2000;
            cliente.close();
        }catch(IOException e){
                e.printStackTrace();
        }
    }
        @Override
        public void run() {
            connection();
            long tsync = ((t1-tcliente)+(t2-t3))/2;

            System.out.println("--------------------------------------------");
            System.out.println("t0 enviado pelo cliente: " + formataData(tcliente));
            System.out.println("Tempo 'real' do Servidor: " + formataData(t1));
            System.out.println("Tempo Envio do Servidor: " + formataData(t2));
            System.out.println("Tempo recebimento Cliente: " + formataData(t3));
            System.out.println("t1-t0: " + (t1-tcliente));
            System.out.println("t2-t3: " + (t2-t3));
            System.out.println("Tempo de sincronização ou defasagem: " + tsync +" milisegundos");
            System.out.println("Horario atual ajustado: " + formataData(System.currentTimeMillis()+tsync));
            System.out.println("--------------------------------------------");
    }
    public long random(){
        long max = 10000;
        long min = 5000;
        double random = (Math.random()*((max-min)+1))+min;
        System.out.println("Random gerado: "+ (long) random);
        return (long) random;
    }
    public String formataData(long data) {
        Timestamp timeStamp = new Timestamp(data);
        Date date = new Date(timeStamp.getTime());

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        String dataFormatada = formato.format(date);

        return dataFormatada;
    }
}
