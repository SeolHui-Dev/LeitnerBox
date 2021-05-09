package com.krakenbook.Billing;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.common.eventbus.Subscribe;
import com.krakenbook.R;

public class BillingModule implements BillingProcessor.IBillingHandler {
    private Context context;
    private BillingProcessor mBillingProcessor;
    String itemId;
    public static boolean is_ad = true;
    public static boolean is_sub = false;

    public BillingModule(Context context) {
        this.context = context;
    }

    public void initBillingProcessor() {
        mBillingProcessor = new BillingProcessor(context, context.getString(R.string.GP_LICENSE_KEY), this);
        mBillingProcessor.initialize();
        // 아래와 차이는 기트허브 페이지에서 확인할 수 있다. 상황에 맞게 사용하면 됨.
        // mBilling Processor = BillingProcessor.newBillingProcessor(context, "rsa_key", this);
        // rsa_key는 개발자 콘솔에서 제공하는 id
    }

    public void purchaseProduct(String itemName) { // 아이템 구매 요청
        setItemId(itemName);
        if(mBillingProcessor.isPurchased(itemId)) {
            Toast.makeText(context, "이미 광고가 제거되었습니다.\n앱 재시작을 권장합니다.", Toast.LENGTH_LONG).show();
            return;
        }
        mBillingProcessor.purchase((Activity)context, itemId);
    }

    public void releaseBillingProcessor() {
        if(mBillingProcessor != null)
            mBillingProcessor.release();
    }

    public BillingProcessor getBillingProcessor() {
        return mBillingProcessor;
    }

    @Override
    public void onProductPurchased(@NonNull String id, @Nullable TransactionDetails transactionDetails) {
        // 아이템 구매 성공 시 호출.
        // 따라서 보상을 지급하든(광고 제거) 혹은 해당 아이템을 소비하든 해당 기능을 작성
    }

    @Override
    public void onPurchaseHistoryRestored() {
        // 구매 내역 복원 및 구매한 모든 PRODUCT ID 목록이 Google Play에서 로드 될 때 호출.
    }

    @Override
    public void onBillingError(int errCode, @Nullable Throwable throwable) {
        // 구매 시 에러가 발생했을 때 처리
        if(errCode != com.anjlab.android.iab.v3.Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            // 사용자가 취소한 게 아니라면 에러 발생에 대해 사용자에게 고지하는 등의 처리
        }
    }

    public void setItemId(String itemName){
        SkuDetails mProduct = mBillingProcessor.getSubscriptionListingDetails(itemName);
        if(mProduct == null)
            return;
        itemId = mProduct.productId;

    }
    @Override
    public void onBillingInitialized() {
        // 개발자 콘솔에서 등록한 아이템 아이디
        SkuDetails mProduct = mBillingProcessor.getSubscriptionListingDetails("1month");
        SkuDetails mProduct_1year = mBillingProcessor.getSubscriptionListingDetails("1year");
        if(mProduct == null || mProduct_1year == null)
            return;
        //itemId = mProduct.productId;
        mBillingProcessor.loadOwnedPurchasesFromGoogle(); // 소유하고 있는 구매 아이템 목록을 가져온다.
        if(mBillingProcessor.isSubscribed(mProduct.productId) || mBillingProcessor.isSubscribed(mProduct_1year.productId)) {
            is_ad = false;
            is_sub = true;
            // 이미 광고 제거를 구매했다면 다시 구매할 필요가 없으므로
            // 해당 부분 처리. 또는 이미 구매 시 광고 제거 구매를 애초에 막는다.
        }
        //main_act.setAD(is_ad);
    }
}