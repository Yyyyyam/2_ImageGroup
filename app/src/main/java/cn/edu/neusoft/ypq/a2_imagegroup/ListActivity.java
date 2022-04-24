package cn.edu.neusoft.ypq.a2_imagegroup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:颜培琦
 * 时间:2022/4/20
 * 功能:ListActivity
 */
public class ListActivity extends AppCompatActivity {

    public static final int IMAGE_LOADING = 0x111;
    public static final int IMAGE_LOADED = 0x110;

    private int[] mStatues = new int[]{0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listName = findViewById(R.id.list_name);

        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0 ; i < 5 ; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "颜培琦");
            map.put("num", String.valueOf(i));
            listItems.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.item_name,
                new String[]{"name", "num"}, new int[]{R.id.tv_name, R.id.tv_num});
        listName.setAdapter(adapter);

        listName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    loadPic();
                    Toast.makeText(ListActivity.this,"load", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
                    Toast.makeText(ListActivity.this , "姓名:" + map.get("name")
                            + ",序号:"+map.get("num") , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadPic() {
        GridView mGridView = findViewById(R.id.grid_pic);
        List<Integer> picId = new ArrayList<>();

        picId.add(R.drawable.transportation_and_vehicle_01);
        picId.add(R.drawable.transportation_and_vehicle_02);
        picId.add(R.drawable.transportation_and_vehicle_03);
        picId.add(R.drawable.transportation_and_vehicle_04);
        picId.add(R.drawable.transportation_and_vehicle_05);
        picId.add(R.drawable.transportation_and_vehicle_06);
        picId.add(R.drawable.transportation_and_vehicle_07);
        picId.add(R.drawable.transportation_and_vehicle_08);
        picId.add(R.drawable.transportation_and_vehicle_09);

        BaseAdapter mPicAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return picId.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(ListActivity.this);
                ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.item_pic, null);
                TextView tvNum = layout.findViewById(R.id.item_pic_num);
                tvNum.setText("第" + (position+1) + "张");
                ImageView imageView = layout.findViewById(R.id.item_pic_iv);
                imageView.setImageResource(picId.get(position));
                ProgressBar progressBar = layout.findViewById(R.id.item_pic_progress);
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == IMAGE_LOADING) {
                            progressBar.setProgress(mStatues[position]);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            mStatues[position] = doWork();
                            Message message = new Message();
                            if (mStatues[position] < 100) {
                                message.what = IMAGE_LOADING;
                                handler.sendMessage(message);
                            } else {
                                message.what = IMAGE_LOADED;
                                handler.sendMessage(message);
                                break;
                            }
                        }
                    }

                    private int doWork() {
                        mStatues[position] += Math.random() * 10;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return mStatues[position];
                    }
                }).start();
                return layout;
            }
        };

        mGridView.setAdapter(mPicAdapter);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(ListActivity.this).create();
                dialog.setTitle("删除");
                dialog.setMessage("是否确认删除该项?");
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", null, null);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        picId.remove(position);
                        mPicAdapter.notifyDataSetChanged();
                        Toast.makeText(ListActivity.this , "删除成功" , Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }
}
