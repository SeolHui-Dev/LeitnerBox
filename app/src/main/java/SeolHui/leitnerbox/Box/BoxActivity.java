package SeolHui.leitnerbox.Box;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

import SeolHui.leitnerbox.MainActivity;
import SeolHui.leitnerbox.MyDialog;
import SeolHui.leitnerbox.R;
import SeolHui.leitnerbox.WordList.WordListViewActivity;

public class BoxActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;

    RecyclerView recyclerView;
    RecyclerView.Adapter Adapter;
    RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private TextView fab_sub1_text, fab_sub2_text;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    public final static int REQUEST_FILE_SELECT = 123;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        mContext = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.box_recycle);
        recyclerView.setHasFixedSize(true);

        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("박스");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFF09BE5));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //플로팅메뉴
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) findViewById(R.id.fab_box_main);
        fab_sub1 = (FloatingActionButton) findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) findViewById(R.id.fab_sub2);
        fab_sub1_text = (TextView) findViewById(R.id.fab_sub1_text);
        fab_sub2_text = (TextView) findViewById(R.id.fab_sub2_text);

        fab_main.setOnClickListener(this);
        fab_sub1.setOnClickListener(this);
        fab_sub2.setOnClickListener(this);


        // Item 리스트에 아이템 객체 넣기
        ArrayList items = new ArrayList<>();
        items.add(new BoxViewItem("1 BOX"));
        items.add(new BoxViewItem("2 BOX"));
        items.add(new BoxViewItem("3 BOX"));
        items.add(new BoxViewItem("4 BOX"));
        items.add(new BoxViewItem("5 BOX"));

        // StaggeredGrid 레이아웃을 사용한다
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        // 지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
        recyclerView.setLayoutManager(layoutManager);

        Adapter = new MyAdpater(items, mContext);
        recyclerView.setAdapter(Adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_SELECT && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            String path= getRealPathFromURI(this,fileUri);
        }
    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;
    }
    public static String getRealPathFromURI(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                } else {
                    String SDcardpath = getRemovableSDCardPath(context).split("/Android")[0];
                    return SDcardpath +"/"+ split[1];
                }
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static String getRemovableSDCardPath(Context context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null)
            return storages[1].toString();
        else
            return "";
    }


    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    @Override//플로팅버튼 눌렀을때 이벤트
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_box_main:
                toggleFab();
                break;
            case R.id.fab_sub1:
                toggleFab();
                //다이얼로그 선언
                MyDialog myDialog = new MyDialog(this, R.layout.dialog_addcard);
                myDialog.callAddExcel();
                break;

            case R.id.fab_sub2:
                toggleFab();

                Toast.makeText(this, "Map Open-!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.plus);
            fab_sub1.startAnimation(fab_close);
            fab_sub2.startAnimation(fab_close);
            fab_sub1_text.startAnimation(fab_close);
            fab_sub2_text.startAnimation(fab_close);
            fab_sub1.setVisibility(View.INVISIBLE);
            fab_sub2.setVisibility(View.INVISIBLE);
            fab_sub1_text.setVisibility(View.INVISIBLE);
            fab_sub2_text.setVisibility(View.INVISIBLE);
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.plus);
            fab_sub1.startAnimation(fab_open);
            fab_sub2.startAnimation(fab_open);
            fab_sub1_text.startAnimation(fab_open);
            fab_sub2_text.startAnimation(fab_open);
            fab_sub1.setVisibility(View.VISIBLE);
            fab_sub2.setVisibility(View.VISIBLE);
            fab_sub1_text.setVisibility(View.VISIBLE);
            fab_sub2_text.setVisibility(View.VISIBLE);
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override//액션바 메뉴 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.box_menu, menu);
        return true;
    }

    @Override//액션바 클릭 이벤트
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈아이콘 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_btn1) {
            Toast.makeText(this, "스케쥴 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_btn2) {
            Toast.makeText(this, "설정 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //외부저장소 확인
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //외부저장소 확인
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //저장하고자 하는 파일의 유형을 지정하는 인수를 받습니다
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("Boxerror", "Directory not created");
        }
        return file;
    }

    // Get the directory for the app's private pictures directory.
    public File getAlbumStorageDir(Context context, String albumName) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("Boxerror", "Directory not created");
        }
        return file;
    }

    class MyAdpater extends RecyclerView.Adapter<BoxActivity.MyAdpater.ViewHolder> {
        private Context context;
        private ArrayList<BoxViewItem> mItems;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public MyAdpater(ArrayList items, Context mContext) {
            mItems = items;
            context = mContext;
        }

        // 필수로 Generate 되어야 하는 메소드 1 : 새로운 뷰 생성
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 새로운 뷰를 만든다
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        // 필수로 Generate 되어야 하는 메소드 2 : ListView의 getView 부분을 담당하는 메소드
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.button.setText(mItems.get(position).boxtitle);

            //setAnimation(holder.imageView, position);
        }

        // // 필수로 Generate 되어야 하는 메소드 3
        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public Button button;

            public ViewHolder(View view) {
                super(view);
                button = (Button) view.findViewById(R.id.box_btn);
            }
        }


    }


}