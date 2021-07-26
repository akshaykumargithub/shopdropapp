package shop.dropapp.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import shop.dropapp.base.viewmodel.MainViewModel;
import shop.dropapp.base.viewmodel.NavDrawerViewModel;
import shop.dropapp.repository.Repository;
import shop.dropapp.ui.addresssection.viewmodel.AddressViewModel;
import shop.dropapp.ui.cartsection.viewmodel.CartViewModel;
import shop.dropapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import shop.dropapp.ui.homesection.viewmodel.CategoriesViewModel;
import shop.dropapp.ui.homesection.viewmodel.HomeViewModel;
import shop.dropapp.ui.loginsection.viewmodel.LoginViewModel;
import shop.dropapp.ui.loginsection.viewmodel.RegisterViewModel;
import shop.dropapp.ui.notificationactivity.viewmodel.NotificationViewModel;
import shop.dropapp.ui.orderssection.viewmodel.OrderViewModel;
import shop.dropapp.ui.product_compare_section.viewmodel.CompareItemViewModel;
import shop.dropapp.ui.product_compare_section.viewmodel.CompareViewmodel;
import shop.dropapp.ui.product_review_section.viewmodel.AddProductReviewViewModel;
import shop.dropapp.ui.product_review_section.viewmodel.ReviewListingViewModel;
import shop.dropapp.ui.productsection.viewmodel.DownloadsViewModel;
import shop.dropapp.ui.productsection.viewmodel.ProductListViewModel;
import shop.dropapp.ui.productsection.viewmodel.ProductViewModel;
import shop.dropapp.ui.profilesection.viewmodel.ProfileViewModel;
import shop.dropapp.ui.searchsection.viewmodel.SearchViewModel;
import shop.dropapp.ui.sellersection.viewmodel.AddVendorReviewViewModel;
import shop.dropapp.ui.sellersection.viewmodel.SellerListViewModel;
import shop.dropapp.ui.sellersection.viewmodel.SellerShopViewModel;
import shop.dropapp.ui.sellersection.viewmodel.WriteVendorReviewViewModel;
import shop.dropapp.ui.wishlistsection.viewmodel.WishListViewModel;

import javax.inject.Inject;


public class ViewModelFactory implements ViewModelProvider.Factory {
    private Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(AddProductReviewViewModel.class))
        {
            return (T) new AddProductReviewViewModel(repository);
        }
        if(modelClass.isAssignableFrom(ReviewListingViewModel.class))
        {
            return (T) new ReviewListingViewModel(repository);
        }
        if(modelClass.isAssignableFrom(CompareViewmodel.class))
        {
            return (T) new CompareViewmodel(repository);
        }
 if(modelClass.isAssignableFrom(CompareItemViewModel.class))
        {
            return (T) new CompareItemViewModel(repository);
        }
if(modelClass.isAssignableFrom(NotificationViewModel.class))
        {
            return (T) new NotificationViewModel(repository);
        }

        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        }
        if (modelClass.isAssignableFrom(NavDrawerViewModel.class)) {
            return (T) new NavDrawerViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ProductListViewModel.class)) {
            return (T) new ProductListViewModel(repository);
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        }
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(repository);
        }
        if (modelClass.isAssignableFrom(AddressViewModel.class)) {
            return (T) new AddressViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CategoriesViewModel.class)) {
            return (T) new CategoriesViewModel(repository);
        }
        if (modelClass.isAssignableFrom(WishListViewModel.class)) {
            return (T) new WishListViewModel(repository);
        }
        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
            return (T) new OrderViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CheckoutViewModel.class)) {
            return (T) new CheckoutViewModel(repository);
        }
        if (modelClass.isAssignableFrom(CheckoutViewModel.class)) {
            return (T) new CheckoutViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SellerListViewModel.class)) {
            return (T) new SellerListViewModel(repository);
        }
        if (modelClass.isAssignableFrom(SellerShopViewModel.class)) {
            return (T) new SellerShopViewModel(repository);
        }
        if (modelClass.isAssignableFrom(WriteVendorReviewViewModel.class)) {
            return (T) new WriteVendorReviewViewModel(repository);
        }
        if (modelClass.isAssignableFrom(AddVendorReviewViewModel.class)) {
            return (T) new AddVendorReviewViewModel(repository);
        }
        if (modelClass.isAssignableFrom(DownloadsViewModel.class)) {
            return (T) new DownloadsViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown class name");
    }
}
