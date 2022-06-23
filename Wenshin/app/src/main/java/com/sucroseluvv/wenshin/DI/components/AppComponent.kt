package com.sucroseluvv.wenshin.DI.components

import com.sucroseluvv.wenshin.DI.modules.AppModule
import com.sucroseluvv.wenshin.DI.modules.NetworkModule
import com.sucroseluvv.wenshin.DI.modules.UserModule
import com.sucroseluvv.wenshin.Screens.GuestScreens.MenuFragments.GuestLoginFragment
import com.sucroseluvv.wenshin.Screens.Common.MainActivity
import com.sucroseluvv.wenshin.Screens.GuestScreens.MenuFragments.MainGuestFragment
import com.sucroseluvv.wenshin.Screens.Common.AuthUserActivity
import com.sucroseluvv.wenshin.Screens.Common.OrderMessagingActivity
import com.sucroseluvv.wenshin.Screens.GuestScreens.RegisterActivity
import com.sucroseluvv.wenshin.Screens.MasterScreens.MenuFragments.OrdersMasterFragment
import com.sucroseluvv.wenshin.Screens.MasterScreens.MenuFragments.ScheduleMasterFragment
import com.sucroseluvv.wenshin.Screens.MasterScreens.OrderInfoMasterActivity
import com.sucroseluvv.wenshin.Screens.MasterScreens.UploadSketchActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.*
import com.sucroseluvv.wenshin.Screens.UserScreens.MenuFragments.MainUserFragment
import com.sucroseluvv.wenshin.Screens.UserScreens.MenuFragments.OrdersUserFragment
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderChoiseMasterUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderChoiseSketchUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderSeansesUserActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class, NetworkModule::class, UserModule::class))
@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(authUserActivity: AuthUserActivity)

    fun inject(mainGuestFragment: MainGuestFragment)
    fun inject(guestLoginFragment: GuestLoginFragment)
    fun inject(registerActivity: RegisterActivity)

    fun inject(mainUserActivity: MainUserActivity)
    fun inject(mainUserFragment: MainUserFragment)
    fun inject(ordersUserFragment: OrdersUserFragment)
    fun inject(ordersMasterFragment: OrdersMasterFragment)
    fun inject(scheduleMasterFragment: ScheduleMasterFragment)
    fun inject(mainActivity: SketchesUserActivity)
    fun inject(orderInfoMasterActivity: OrderInfoMasterActivity)
    fun inject(uploadSketchActivity: UploadSketchActivity)

    fun inject(newOrderUserActivity: NewOrderUserActivity)
    fun inject(newOrderChoiseSketchUserActivity: NewOrderChoiseSketchUserActivity)
    fun inject(newOrderChoiseMasterUserActivity: NewOrderChoiseMasterUserActivity)
    fun inject(newOrderSeansesUserActivity: NewOrderSeansesUserActivity)
    fun inject(sketchInfoUserActivity: SketchInfoUserActivity)
    fun inject(favoritesUserActivity: FavoritesUserActivity)
    fun inject(historyUserActivity: HistoryUserActivity)
    fun inject(sketchesSearchUserActivity: SketchesSearchUserActivity)
    fun inject(historyOrderInfoUserActivity: HistoryOrderInfoUserActivity)
    fun inject(orderInfoUserActivity: OrderInfoUserActivity)
    fun inject(orderMessagingActivity: OrderMessagingActivity)
    fun inject(settingsActivity: UserSettingsActivity)
    fun inject(masterInfoUserActivity: MasterInfoUserActivity)
    fun inject(createConsultationActivity: CreateConsultationActivity)

}