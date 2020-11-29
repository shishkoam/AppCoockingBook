package ua.shishkoam.appcoockingbook.oldutils.interactors;

/**
 * @author uncle_doc
 * @date 07.12.2015
 */
public class NotificationHelper {

    public static final String KEY_RESULT = "KEY_RESULT";

// TODO Implement
//    public static void notify(Emplacement emplacement) {
//        Context context = GlobalSettings.INSTANCE.getContext();
//        if (context == null || emplacement == null || emplacement.status == null)
//            return;
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(context.getString(R.string.notification_title_status_changed, emplacement.name))
//                .setContentText(context.getResources().getStringArray(R.array.EmplStatus)[emplacement.status.ordinal()]);
//
//        Intent resultIntent = new Intent(context, MainActivity.class);
//        resultIntent.setAction(Long.toString(System.currentTimeMillis()));
//        resultIntent.putExtra(KEY_RESULT, emplacement.guid);
//
//        builder.setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(emplacement.guid.hashCode(), builder.build());
//    }
}
