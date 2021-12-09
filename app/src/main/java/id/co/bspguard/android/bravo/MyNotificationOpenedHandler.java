package id.co.bspguard.android.bravo;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import id.co.bspguard.android.bravo.calculator.Calculator;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.news.MainNews;
import id.co.bspguard.android.bravo.notifications.MainNotifications;
import id.co.bspguard.android.bravo.notifications.NotificationDataSet;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    private Application application;
    int id, idNotification;
    String type, typeNotification;
    Fungsi fn = new Fungsi();
    String url = fn.url();
    VolleyObjectService vos;
    VolleyObjectResult vor;


    public MyNotificationOpenedHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {

        // Get custom datas from notification
        JSONObject data = result.notification.payload.additionalData;
        if (data != null) {
            String myCustomData = data.optString("user_id", null);
            id = data.optInt("id");
            type = data.optString("type");
            Log.i("ini adalah user_id", data.toString());
        }

        // React to button pressed
        OSNotificationAction.ActionType actionType = result.action.type;
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
          Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        }

        // Launch new activity using Application object
        startApp(data);
    }

  private void startApp(JSONObject data) {

      updateData(data.optString("id_notification"));
      if(data.optString("type").equals("article")){
        Intent intent = new Intent(application, MainNews.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
      }

  }

  private void updateData(String id) {
    String urlAction = url+"read-notification/"+id;

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {
        try {
          Log.d("News", response.toString());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
        Toast.makeText(application, error.toString(), Toast.LENGTH_LONG).show();
      }
    };

    vos = new VolleyObjectService(vor, application);
    vos.getJsonObject("GETCALL", urlAction);


  }

//  private void startApp(idNotification, typeNotification) {
//        Intent intent = new Intent(application, Calculator.class)
//                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//        application.startActivity(intent);
//    }
}
