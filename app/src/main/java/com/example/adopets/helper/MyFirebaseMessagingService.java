package com.example.adopets.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.adopets.R;
import com.example.adopets.activity.HomeActivity;
import com.example.adopets.activity.PerfilAnimalDoacaoActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //chamado quando o usuario recebe uma notificação com o app aberto
    @Override
    public void onMessageReceived(RemoteMessage notificacao) {
        if(notificacao.getNotification()!= null){
            String titulo = notificacao.getNotification().getTitle();
            String corpo = notificacao.getNotification().getBody();
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    private void enviarNotificação(String titulo, String corpo){
        //configuração para notificação
        String canal = getString(R.string.default_notification_channel_id);
        Uri uriSom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, PerfilAnimalDoacaoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //criar notificação
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this,canal)
                .setContentTitle(titulo)
                .setContentText(corpo)
                .setSmallIcon(R.drawable.ic_animal)
                .setSound(uriSom)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        //Recuperar notificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //verifica a versão do android a partir do Oreo para configurar canal de notificação
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(canal,"canal",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        //envia notificação
        notificationManager.notify(0,notificacao.build());

    }
}
