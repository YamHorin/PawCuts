import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pawcuts.databinding.FragmentMenuBarBarberBinding
import com.example.pawcuts.interfaces.CallBackMenuBarberButtonsPress


class MenuBarBarberFragment : Fragment() {
    private lateinit var binding:FragmentMenuBarBarberBinding
    var callBackMenuBarberButtonsPress:CallBackMenuBarberButtonsPress? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentMenuBarBarberBinding.inflate(inflater, container, false)
        binding.menuBarBarberFragmentNavigationProfile.setOnClickListener({
            callBackMenuBarberButtonsPress?.profile()
        })
        binding.menuBarBarberFragmentNavigationLogOut.setOnClickListener({
            callBackMenuBarberButtonsPress?.logOut()
        })
        binding.menuBarBarberFragmentNavigationReviews.setOnClickListener({
            callBackMenuBarberButtonsPress?.reviews()
        })
        binding.menuBarBarberFragmentNavigationCalendar.setOnClickListener({
            callBackMenuBarberButtonsPress?.appointments()
        })
        return binding.root
    }

}
