package SeolHui.leitnerbox.WordList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import SeolHui.leitnerbox.Box.BoxActivity;
import SeolHui.leitnerbox.R;

public class WordListViewActivity extends AppCompatActivity {
    Context mContext;

    RecyclerView recyclerView;
    RecyclerView.Adapter Adapter, AdapterList;
    RecyclerView.LayoutManager layoutManager,layoutManager2;
    ArrayList items = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlistview);
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("단어장");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFF09BE5));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //액션바 숨기기
        //hideActionBar();
        mContext = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);


        // Item 리스트에 아이템 객체 넣기
        // ArrayList items = new ArrayList<>();

        items.add(new WordListViewItem(R.drawable.n2, "미키마우스"));
        items.add(new WordListViewItem(R.drawable.n1, "인어공주"));
        items.add(new WordListViewItem(R.drawable.n3, "디즈니공주"));
        items.add(new WordListViewItem(R.drawable.n5, "토이스토리"));
        items.add(new WordListViewItem(R.drawable.n2, "미키마우스"));
        items.add(new WordListViewItem(R.drawable.n1, "인어공주"));
        items.add(new WordListViewItem(R.drawable.n3, "디즈니공주"));
        items.add(new WordListViewItem(R.drawable.n5, "토이스토리"));
        items.add(new WordListViewItem(R.drawable.n2, "미키마우스"));
        items.add(new WordListViewItem(R.drawable.n1, "인어공주"));
        items.add(new WordListViewItem(R.drawable.n3, "디즈니공주"));
        items.add(new WordListViewItem(R.drawable.n5, "토이스토리"));
        items.add(new WordListViewItem(R.drawable.n2, "미키마우스"));
        items.add(new WordListViewItem(R.drawable.n1, "인어공주"));
        items.add(new WordListViewItem(R.drawable.n3, "디즈니공주"));
        items.add(new WordListViewItem(R.drawable.n5, "토이스토리"));
        items.add(new WordListViewItem(R.drawable.n2, "미키마우스"));
        items.add(new WordListViewItem(R.drawable.n1, "인어공주"));
        items.add(new WordListViewItem(R.drawable.n3, "디즈니공주"));
        items.add(new WordListViewItem(R.drawable.n5, "토이스토리"));
        items.add(new WordListViewItem(R.drawable.n2, "미키마우스"));
        items.add(new WordListViewItem(R.drawable.n1, "인어공주"));
        items.add(new WordListViewItem(R.drawable.n3, "디즈니공주"));
        items.add(new WordListViewItem(R.drawable.n5, "토이스토리"));

        // StaggeredGrid 레이아웃을 사용한다
        layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        layoutManager2 = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        //layoutManager = new LinearLayoutManager(this);
        //layoutManager = new GridLayoutManager(this,3);
        //지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
        recyclerView.setLayoutManager(layoutManager);

        Adapter = new MyAdpater(items,mContext, false);
        AdapterList = new MyAdpater(items,mContext, true);
        recyclerView.setAdapter(Adapter);

    }
    @Override//액션바 메뉴 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wordlistview_menu, menu);
        return true;
    }
    @Override//액션바 클릭이벤트
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈아이콘 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_btn1) {
            Toast.makeText(this, "전환 클릭", Toast.LENGTH_SHORT).show();
            if(recyclerView.getLayoutManager() ==  layoutManager){
                recyclerView.setLayoutManager(layoutManager2);
                recyclerView.setAdapter(AdapterList);
                ((MyAdpater)AdapterList).notifyDataSetChanged();
            }
            else {
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(Adapter);
                ((MyAdpater)Adapter).notifyDataSetChanged();
            }
            return true;
        }
        if (id == R.id.action_btn2) {
            Toast.makeText(this, "설정 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class MyAdpater extends RecyclerView.Adapter<MyAdpater.ViewHolder>
    {
        private Context context;
        private ArrayList<WordListViewItem> mItems;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public MyAdpater(ArrayList items, Context mContext, boolean isList)
        {
            mItems = items;
            context = mContext;
            this.isList = isList;
        }
        boolean isList;

        // 필수로 Generate 되어야 하는 메소드 1 : 새로운 뷰 생성
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 새로운 뷰를 만든다
            ViewHolder holder = null;

            if(isList){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item, parent, false);
                holder = new ViewHolder(v);
            }
            else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordview_item, parent, false);
                holder = new ViewHolder(v);
            }
            return holder;
        }

        // 필수로 Generate 되어야 하는 메소드 2 : ListView의 getView 부분을 담당하는 메소드
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.imageView.setImageResource(mItems.get(position).image);
            holder.textView.setText(mItems.get(position).imagetitle);

            //setAnimation(holder.imageView, position);
        }

        // // 필수로 Generate 되어야 하는 메소드 3
        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder  extends RecyclerView.ViewHolder {

            public LinearLayout linearLayout;
            public ImageView imageView;
            public TextView textView;

            public ViewHolder(View view) {
                super(view);
                if(isList){
                    linearLayout = (LinearLayout) view.findViewById(R.id.ll_w_il);
                    imageView = (ImageView) view.findViewById(R.id.wordlist_picture);
                    textView = (TextView) view.findViewById(R.id.picture_titlelist);
                }
                else {
                    linearLayout = (LinearLayout) view.findViewById(R.id.ll_w_i);
                    imageView = (ImageView) view.findViewById(R.id.word_picture);
                    textView = (TextView) view.findViewById(R.id.picture_title);
                }
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //단어장 클릭
                        Intent intent = new Intent(WordListViewActivity.this, BoxActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

    }

}