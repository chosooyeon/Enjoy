package com.example.sooyeon.graduationproject.tab;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sooyeon.graduationproject.R;
import com.example.sooyeon.graduationproject.loginTab.PagerAdatper2;

public class MypageFragment extends Fragment {
    private Button btnJoinEdit;
    private Button btnLogout;
    private Button btnQnA;

    private TabLayout mTabLayout2;
    private ViewPager mPager2;

    public MypageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        //TODO 여기에 View를 찾고 이벤트를 등록하고 등등의 처리를 할 수 있다.
        //getView().findViewById(R.id.btn1).setOnClickListener();
        //btnJoinEdit = getView().findViewById(R.id.btnJoinEdit);
        //btnLogout = getView().findViewById(R.id.btnLogout);
        //btnQnA = getView().findViewById(R.id.btnQnA);


        mTabLayout2 = (TabLayout) view.findViewById(R.id.tabLayout2);
        mPager2 = (ViewPager) view.findViewById(R.id.pager2);

        mTabLayout2.addTab(mTabLayout2.newTab().setText("star"));
        mTabLayout2.addTab(mTabLayout2.newTab().setText("info"));

        mTabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);//탭의 가로 전체 사이즈 지정
        mTabLayout2.setTabMode(TabLayout.MODE_FIXED);

        //탭 아이콘 추가
        mTabLayout2.getTabAt(0).setIcon(R.drawable.star);
        mTabLayout2.getTabAt(1).setIcon(R.drawable.account);

        //프래그먼트를 관리할 PagerAdapter를 생성한다.
        PagerAdatper2 pagerAdatper = new PagerAdatper2(getChildFragmentManager(),mTabLayout2.getTabCount());
        mPager2.setAdapter(pagerAdatper);

        //Tab 레이아웃과 ViewPager를 이벤트로 서로 연결시켜준다.
        //ViewPager가 움직였을 때, 탭이 바뀌게끔 한다.
        mPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout2));

        //탭레이아웃이 바뀌면 ViewPager의 Fragment도 바뀌는 작업을 연결한다.
        mTabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //현재 사용자가 바꾼 탭의 이벤트가 넘어온다.
                mPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
