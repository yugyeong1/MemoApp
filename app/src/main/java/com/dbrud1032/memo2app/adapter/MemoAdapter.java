package com.dbrud1032.memo2app.adapter;

// 1. RecyclerView.Adapter 를 상속받는다.

// 2. 상속받은 클래스가 abstract 이므로, unimplemented method 오버라이드!

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dbrud1032.memo2app.EditActivity;
import com.dbrud1032.memo2app.MainActivity;
import com.dbrud1032.memo2app.R;
import com.dbrud1032.memo2app.data.DatabaseHandler;
import com.dbrud1032.memo2app.model.Memo;

import java.util.ArrayList;
import java.util.List;


// 6. RecyclerView.Adapter 의 데이터 타입을 적어주어야 한다.
//    우리가 만든 ViewHolder 로 적는다.


// 7. 빨간색 에러가 뜨면, 우리가 만든 ViewHolder 로
// onCreateViewHolder, onBindViewHolder 변경해준다.

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    // 5. 어댑터 클래스의 멤버변수와 생성자를 만들어 준다.
    Context context;
    List<Memo> memoList;

    // 유저가 삭제를 누른 인덱스
    int deleteIndex;


    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;

    }


    // 8. 오버라이드 함수 3개를 전부 구현 해주면 끝!
    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_row, parent, false);

        return new MemoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        // 뷰에 데이터를 셋팅한다!
        Memo memo = memoList.get(position);

        holder.txtTitle.setText(memo.title);
        holder.txtContent.setText(memo.content);

    }

    @Override
    public int getItemCount() {
        // 전체 데이터의 갯수를 적어준다.
        return memoList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //      이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    // RecyclerView.ViewHolder 상속받는다.

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtContent;
        CardView cardView;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 1. 인텐트에 유저가 어떤 행을 눌렀는지 파악하며
                    // 누른 이름과 전화번호를 담아서
                    int index = getAdapterPosition();

                    Memo memo = memoList.get(index);

                    // 2. 수정 액티비티를 띄운다.
                    //    어떤 액티비티가 어떤 액티비티를 띄운다!! => 인텐트에 있어야 한다.
                    Intent intent = new Intent(context, EditActivity.class);

                    intent.putExtra("memo", memo);

//                    intent.putExtra("id",contact.id);
//                    intent.putExtra("name", contact.name);
//                    intent.putExtra("phone", contact.phone);

                    context.startActivity(intent);

                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 1. 어느 주소록을 삭제할 것인지
                    // 삭제할 주소록을 먼저 가져온다.
                    deleteIndex = getAdapterPosition();

                    // 2. 알러트 다이얼로그가 나온다.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("메모 삭제");
                    builder.setMessage("정말 삭제하시겠습니까?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 3. 알러트 다이얼로그에서 Yes 눌렀을 때
                            //    삭제할 주소록을 데이터베이스에서 삭제

                            DatabaseHandler db = new DatabaseHandler(context);
                            Memo memo = memoList.get(deleteIndex);

                            db.deleteMemo(memo);

                            // 알러트 다이얼로그는 액티비티가 아니므로
                            // 메인엑티비티의 onResume 함수가 실행안된다.
                            // 따라서 화면 갱신이 안된다.

                            // 즉, 디비에 저장된 데이터 삭제했으니,
                            // 메모리에 저장된 데이터도 삭제한다.
                            memoList.remove(deleteIndex);
                            // 데이터가 변경되었으니, 화면 갱신하라고 함수 호출
                            notifyDataSetChanged();

                        }
                    });

                    builder.setNegativeButton("No", null);
                    builder.show();

                }
            });







        }
    }
}
