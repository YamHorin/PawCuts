package com.example.pawcuts.fragments.petOwnerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pawcuts.R
import com.example.pawcuts.interfaces.CallBackMenuPetOwnerButtonsPress
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


class menuPetOwnerFragment : Fragment() {

    private lateinit var navigation_FAB_findBarbers : FloatingActionButton
    private lateinit var navigation_events : MaterialButton
    private lateinit var navigation_profile :    MaterialButton
    private lateinit var navigation_favorites :MaterialButton
    private lateinit var navigation_logOut :    MaterialButton
    var callBackMenuPetOwnerButtonsPress: CallBackMenuPetOwnerButtonsPress? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_menu_pet_owner, container, false)
        findViews(v)
        initViews(v)
        return v
    }

    private fun findViews(v: View) {
        navigation_FAB_findBarbers = v.findViewById(R.id.navigation_FAB_findBarbers)
        navigation_events = v.findViewById(R.id.navigation_events)
        navigation_profile = v.findViewById(R.id.navigation_profile)
        navigation_favorites = v.findViewById(R.id.navigation_favorites)
        navigation_logOut = v.findViewById(R.id.navigation_logOut)

    }

    private fun initViews(v: View?) {
        navigation_FAB_findBarbers.setOnClickListener({callBackMenuPetOwnerButtonsPress?.barbers()})
        navigation_events.setOnClickListener({callBackMenuPetOwnerButtonsPress?.appointments()})
        navigation_profile.setOnClickListener({callBackMenuPetOwnerButtonsPress?.profile()})
        navigation_favorites.setOnClickListener({callBackMenuPetOwnerButtonsPress?.favorites()})
        navigation_logOut.setOnClickListener({callBackMenuPetOwnerButtonsPress?.logOut()})

    }


}