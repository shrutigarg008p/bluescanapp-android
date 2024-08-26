package com.sixdegreesit.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixdegreesit.securityapp.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private ExpandableListView mExpandableListView;
	private List<GroupEntity> mGroupCollection;
	private int[] groupStatus;

	public ExpandableListAdapter(Context pContext,ExpandableListView pExpandableListView,List<GroupEntity> pGroupCollection) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];
		setListEvent();
	}

	private void setListEvent() {mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int arg0) {
						groupStatus[arg0] = 1;
					}
				});

		mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					@Override
					public void onGroupCollapse(int arg0) {
						groupStatus[arg0] = 0;
					}
				});
	}

	@Override
	public String getChild(int arg0, int arg1) {
		return mGroupCollection.get(arg0).GroupItemCollection.get(arg1).question;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return 0;
	}

	@Override
	public View getChildView(final int arg0, final int arg1, boolean arg2, View arg3,ViewGroup arg4) {
			final ChildHolder childHolder;
		if (arg3 == null) {
			arg3 = LayoutInflater.from(mContext).inflate(R.layout.list_item, arg4,false);
			childHolder = new ChildHolder();
			childHolder.tvQuestion = (TextView) arg3.findViewById(R.id.fieldQuestionTextView);
			childHolder.ivCheck=(ImageView)arg3.findViewById(R.id.ivCheckQuestion);
			arg3.setTag(childHolder);
		}else {
			childHolder = (ChildHolder) arg3.getTag();
		}
		childHolder.tvQuestion.setText(mGroupCollection.get(arg0).GroupItemCollection.get(arg1).question);	
		
		if (mGroupCollection.get(arg0).GroupItemCollection.get(arg1).questionType.equalsIgnoreCase("select")) {
			childHolder.ivCheck.setVisibility(View.VISIBLE);
			if (mGroupCollection.get(arg0).GroupItemCollection.get(arg1).isChecked.equals("true")) {
				childHolder.ivCheck.setImageResource(R.drawable.icon_checked);
			} else {
				childHolder.ivCheck.setImageResource(R.drawable.icon_unchecked);
			}
		}else{
			if (mGroupCollection.get(arg0).GroupItemCollection.get(arg1).isChecked.equals("true")) {
				childHolder.ivCheck.setImageResource(R.drawable.icon_checked);
				childHolder.ivCheck.setVisibility(View.VISIBLE);
			} else {
				childHolder.ivCheck.setImageResource(R.drawable.icon_unchecked);
				childHolder.ivCheck.setVisibility(View.INVISIBLE);
			}
			
		}
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return mGroupCollection.get(arg0).GroupItemCollection.size();
	}
	@Override
	public Object getGroup(int arg0) {
		return mGroupCollection.get(arg0);
	}
	@Override
	public int getGroupCount() {
		return mGroupCollection.size();
	}
	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}
	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		GroupHolder groupHolder;
		if (arg2 == null) {
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.list_group,arg3,false);
			groupHolder = new GroupHolder();
			groupHolder.img = (ImageView) arg2.findViewById(R.id.iv_arrow);
			groupHolder.title = (TextView) arg2.findViewById(R.id.lblListHeader);
			arg2.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) arg2.getTag();
		}
				
//		if(mGroupCollection.get(arg0).GroupItemCollection.size()==0){
//			groupHolder.img.setVisibility(View.GONE);
//		}else{
//			groupHolder.img.setVisibility(View.VISIBLE);
//		} 
		
		if(mGroupCollection.get(arg0).GroupItemCollection.size()>0){
			if (groupStatus[arg0] == 0) {
				groupHolder.img.setImageResource(R.drawable.arrow_right_white);
			} else {
				groupHolder.img.setImageResource(R.drawable.arrow_down_white);
			}
		}
		
		if (mGroupCollection.get(arg0).isMandatory.equals("1")) {
			groupHolder.title.setText(Html.fromHtml(mGroupCollection.get(arg0).question+"<font color='#ff0000'> *</font>"));
		} else {
			groupHolder.title.setText(Html.fromHtml(mGroupCollection.get(arg0).question));
		}
		return arg2;
	}

	class GroupHolder {
		ImageView img;
		TextView title;
	}

	class ChildHolder {
		TextView tvQuestion;
		ImageView ivCheck;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
