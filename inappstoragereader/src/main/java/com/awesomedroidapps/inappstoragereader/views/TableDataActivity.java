package com.awesomedroidapps.inappstoragereader.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.awesomedroidapps.inappstoragereader.AppStorageDataRecyclerView;
import com.awesomedroidapps.inappstoragereader.Constants;
import com.awesomedroidapps.inappstoragereader.DataItemDialogFragment;
import com.awesomedroidapps.inappstoragereader.DatabaseQueryCommands;
import com.awesomedroidapps.inappstoragereader.ErrorType;
import com.awesomedroidapps.inappstoragereader.QueryDatabaseAsyncTask;
import com.awesomedroidapps.inappstoragereader.R;
import com.awesomedroidapps.inappstoragereader.TableDataAsyncTask;
import com.awesomedroidapps.inappstoragereader.Utils;
import com.awesomedroidapps.inappstoragereader.adapters.TableDataListAdapter;
import com.awesomedroidapps.inappstoragereader.entities.QueryDataResponse;
import com.awesomedroidapps.inappstoragereader.entities.TableDataResponse;
import com.awesomedroidapps.inappstoragereader.interfaces.CommandResponses;
import com.awesomedroidapps.inappstoragereader.interfaces.DataItemClickListener;
import com.awesomedroidapps.inappstoragereader.interfaces.ErrorMessageInterface;
import com.awesomedroidapps.inappstoragereader.interfaces.QueryResponseListener;
import com.awesomedroidapps.inappstoragereader.interfaces.TableDataEditListener;
import com.awesomedroidapps.inappstoragereader.interfaces.TableDataView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by anshul on 11/2/17.
 */

public class TableDataActivity extends AppCompatActivity
    implements ErrorMessageInterface, DataItemClickListener, TableDataView,
    TableDataEditListener, QueryResponseListener,CommandResponses {

  private AppStorageDataRecyclerView tableDataRecyclerView;
  private String databaseName, tableName;
  private ProgressDialog progressDialog;
  private RelativeLayout errorHandlerLayout;
  private List<String> tableColumnNames;
  private List<Integer> tableColumnTypes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.com_awesomedroidapps_inappstoragereader_activity_table_data);
    tableDataRecyclerView =
        (AppStorageDataRecyclerView) findViewById(R.id.table_data_recycler_view);
    errorHandlerLayout = (RelativeLayout) findViewById(R.id.error_handler);
    progressDialog = new ProgressDialog(this);

    //Read the bundle
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      databaseName = bundle.getString(Constants.BUNDLE_DATABASE_NAME);
      tableName = bundle.getString(Constants.BUNDLE_TABLE_NAME);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    initUI();
    fetchData();
  }

  private void fetchData(){
    new TableDataAsyncTask(new WeakReference(this), this).execute(
        new String[]{databaseName, tableName});
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.com_awesomedroidapps_inappstoragereader_table_data, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    if (item.getItemId() == R.id.com_awesomedroidapps_inappstoragereader_edit) {
      openQueryActivity();
    }
    return super.onOptionsItemSelected(item);
  }

  private void openQueryActivity() {
    Intent intent = new Intent(this, QueryDatabaseActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.BUNDLE_DATABASE_NAME, databaseName);
    bundle.putString(Constants.BUNDLE_TABLE_NAME,tableName);
    intent.putExtras(bundle);
    startActivity(intent);
  }

  private void initUI() {
    tableDataRecyclerView.setVisibility(View.GONE);
    errorHandlerLayout.setVisibility(View.GONE);
    progressDialog.setMessage(
        getString(R.string.com_awesomedroidapps_inappstoragereader_progressBar_message));
    progressDialog.setIndeterminate(false);
    progressDialog.show();
  }


  @Override
  public void handleError(ErrorType errorType) {

  }

  @Override
  public void onDataItemClicked(String data) {

   }

  @Override
  public void onDataItemClicked(String data, int columnIndex, List<String> columnValues) {
    if (Utils.isEmpty(data)) {
      String toastMessage =
          getResources().getString(R.string.com_awesomedroidapps_inappstoragereader_item_empty);
      Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
      return;
    }

    DataItemDialogFragment dataItemDialogFragment = DataItemDialogFragment.newInstance(data,this,
        columnIndex,columnValues);
    dataItemDialogFragment.show(getSupportFragmentManager(), "dialog");

  }

  @Override
  public void onDataFetched(TableDataResponse tableDataResponse) {

    progressDialog.dismiss();

    if (tableDataResponse == null || Utils.isEmpty(tableDataResponse.getTableData())) {
      handleError(ErrorType.NO_TABLE_DATA_FOUND);
      return;
    }

    tableColumnNames = tableDataResponse.getColumnNames();
    tableColumnTypes = tableDataResponse.getColumnTypes();
    tableDataRecyclerView.setVisibility(View.VISIBLE);
    tableDataRecyclerView.setRecyclerViewWidth(tableDataResponse.getRecyclerViewWidth());


    TableDataListAdapter adapter =
        new TableDataListAdapter(tableDataResponse.getTableData(), this,
            tableDataResponse.getRecyclerViewColumnsWidth(), this);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager
        .VERTICAL, false);
    Utils.setActionBarTitle(getSupportActionBar(), tableName,
        tableDataResponse.getTableData().size() - 1);
    tableDataRecyclerView.setLayoutManager(linearLayoutManager);
    tableDataRecyclerView.setAdapter(adapter);
  }

  @Override
  public void onTableDataEdited(String newValue, int columnIndex,
                                List<String> columnValues) {
    String toUpdateColumn = tableColumnNames.get(columnIndex);

    String whereClause = Constants.EMPTY_STRING;
    StringBuilder stringBuilder = new StringBuilder(" WHERE ");
    for(int i=0;i<columnValues.size();i++){
      if(tableColumnTypes.get(i)==Cursor.FIELD_TYPE_NULL){
        continue;
      }
      stringBuilder.append(tableColumnNames.get(i));
      stringBuilder.append(Constants.EQUAL);
      if(tableColumnTypes.get(i).equals(Cursor.FIELD_TYPE_STRING)){
        stringBuilder.append(Constants.INVERTED_COMMA);
      }
      stringBuilder.append(columnValues.get(i));
      if(tableColumnTypes.get(i).equals(Cursor.FIELD_TYPE_STRING)){
        stringBuilder.append(Constants.INVERTED_COMMA);
      }
      if(i==columnValues.size()-1){
        break;
      }
      stringBuilder.append(Constants.SPACE);
      stringBuilder.append(Constants.AND);
      stringBuilder.append(Constants.SPACE);
    }
    whereClause = stringBuilder.toString();
    int toUpdateColumnType = tableColumnTypes.get(columnIndex);
    if(toUpdateColumnType==Cursor.FIELD_TYPE_STRING){
      newValue = Constants.INVERTED_COMMA+newValue+Constants.INVERTED_COMMA;
    }
    whereClause=whereClause.trim();

    String updateQuery = "update "+tableName+" set "+toUpdateColumn+"="+newValue+Constants
        .SPACE+whereClause;
    new QueryDatabaseAsyncTask(new WeakReference(this), this).execute(new String[]{
        databaseName, updateQuery});
  }


  @Override
  public void onSelectQueryResponse(QueryDataResponse queryDataResponse) {
    switch (queryDataResponse.getQueryStatus()) {
      case SUCCESS:
        break;
      case FAILURE:
        break;
    }
  }

  @Override
  public void onUpdateQueryResponse(QueryDataResponse queryDataResponse) {
    switch (queryDataResponse.getQueryStatus()) {
      case SUCCESS:
        Toast.makeText(this,"Update successful",Toast.LENGTH_LONG).show();
        fetchData();
        break;
      case FAILURE:
        Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
        break;
    }
  }

  @Override
  public void onDeleteQueryResponse(QueryDataResponse queryDataResponse) {

  }

  @Override
  public void onInsertQueryResponse(QueryDataResponse queryDataResponse) {

  }

  @Override
  public void onDataFetched(QueryDataResponse queryDataResponse) {
    progressDialog.dismiss();

    if (queryDataResponse == null || queryDataResponse.getQueryStatus() == null) {
      return;
    }

    DatabaseQueryCommands commands = queryDataResponse.getDatabaseQueryCommands();

    if(commands!=null){
      switch (commands){
        case UPDATE:
          onUpdateQueryResponse(queryDataResponse);
          break;
        case SELECT:
          onSelectQueryResponse(queryDataResponse);
          break;
        case DELETE:
          onDeleteQueryResponse(queryDataResponse);
          break;
        case INSERT:
          onInsertQueryResponse(queryDataResponse);
          break;
      }
    }

  }
}
