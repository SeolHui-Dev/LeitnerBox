package SeolHui.leitnerbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import static SeolHui.leitnerbox.Box.BoxActivity.REQUEST_FILE_SELECT;

public class MyDialog {

    private Activity context;
    private int layout;

    public MyDialog(Activity context, int layout) {
        this.context = context;
        this.layout = layout;
    }


    public void callAddExcel(){
        final View innerView = context.getLayoutInflater().inflate(layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        Button ac_exbtn =  innerView.findViewById(R.id.ac_exbtn);
        Button ac_jubtn =  innerView.findViewById(R.id.ac_jubtn);


        builder.setView(innerView);

        final AlertDialog dialog = builder.create();

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ac_exbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
                context.startActivityForResult(Intent.createChooser(intent, "골라"), REQUEST_FILE_SELECT);
                dialog.cancel();
            }
        });

        dialog.show();
    }

}
