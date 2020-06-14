package com.example.withcafe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SubwaySearchActivity extends AppCompatActivity {
    private SubwaySearchAdapter subwaySearchAdapter;
    private List<String> list;
    private ListView listView;
    private EditText editSearch;
    private ArrayList<String> searchArrayList;
    private String subline;
    private String sibal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subway);

        Intent intent = getIntent();
        subline = intent.getExtras().getString("노선검색");

        editSearch = (EditText)findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        list = new ArrayList<String>();
        settingList();

        searchArrayList = new ArrayList<String>();
        searchArrayList.addAll(list);

        subwaySearchAdapter = new SubwaySearchAdapter(list, this);

        listView.setAdapter(subwaySearchAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Global_Variable Gb_SubwayName = (Global_Variable)getApplication();
                Gb_SubwayName.setGb_SubwayName(searchArrayList.get(position));

                finish();
            }
        });



        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

    }

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(searchArrayList);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < searchArrayList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (searchArrayList.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(searchArrayList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        subwaySearchAdapter.notifyDataSetChanged();
    }

    private void settingList(){


        try {
            InputStream is = getBaseContext().getResources().getAssets().open("university_list.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if( wb != null) {
                Sheet sheet = wb.getSheet(0); // 시트 불러오기
                if(sheet != null) {
                    int rowIndexStart = 0;
                    int rowTotal = 813; // 전체 행의 수 : 시트의 colTotal-1 의 길이
                    Log.d("로그1", subline);
                    for(int row = rowIndexStart ; row<rowTotal ; row++){
                        Log.i("로그2", sheet.getCell(3, row).getContents());
                        if( subline.equals(sheet.getCell(3, row).getContents())) {
                            String contents = sheet.getCell(4, row).getContents(); // 시트의 행, 열에 해당하는 셀을 넣는다.
                            list.add(contents);
                        }
                    }
                }
            }
        }catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }
}
