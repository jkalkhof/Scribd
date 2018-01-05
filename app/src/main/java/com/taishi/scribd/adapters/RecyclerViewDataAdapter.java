package com.taishi.scribd.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.taishi.scribd.R;
import com.taishi.scribd.models.SectionDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yamasakitaishi on 2016/11/24.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

	private ArrayList<SectionDataModel> dataList;
	private Context mContext;
	private AppCompatActivity activity;

	public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList,AppCompatActivity activity) {
		this.dataList = dataList;
		this.mContext = context;
		this.activity = activity;
	}

	@Override
	public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
		ItemRowHolder mh = new ItemRowHolder(v);
		return mh;
	}

	@Override
	public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

		final String sectionName = dataList.get(i).getHeaderTitle();

		List singleSectionItems = dataList.get(i).getAllItemsInSection();

		itemRowHolder.itemTitle.setText(sectionName);

		Log.d("RecyclerViewDataAdaper"," onBindViewHolder["+i+"]: " + sectionName);

		SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems, activity);

		itemRowHolder.recycler_view_list.setHasFixedSize(true);
		itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
		itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


		itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

			}
		});

	}

	@Override
	public int getItemCount() {
		return (null != dataList ? dataList.size() : 0);
	}

	public class ItemRowHolder extends RecyclerView.ViewHolder {

		protected TextView itemTitle;

		protected RecyclerView recycler_view_list;

		protected Button btnMore;



		public ItemRowHolder(View view) {
			super(view);

			this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
			this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
			this.btnMore= (Button) view.findViewById(R.id.btnMore);

		}

	}

}