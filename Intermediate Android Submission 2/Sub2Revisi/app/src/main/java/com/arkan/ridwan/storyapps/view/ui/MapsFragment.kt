package com.arkan.ridwan.storyapps.view.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.arkan.ridwan.storyapps.BuildConfig
import com.arkan.ridwan.storyapps.R
import com.arkan.ridwan.storyapps.databinding.FragmentMapsBinding
import com.arkan.ridwan.storyapps.viewmodel.StoryVM
import com.arkan.ridwan.storyapps.viewmodel.UserVM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private val storyViewModel by viewModels<StoryVM>()
    private val userViewModel by viewModels<UserVM>()
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(
            androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar,
            true
        );
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        navController = Navigation.findNavController(view)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setStyle()
        setCameraMovement()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        userViewModel.getUserPreferences().observe(this) {
            if (it.token.trim() != "") {
                storyViewModel.getStoryWithLocation(it.token)
            }
        }

        storyViewModel.dataStoryWithLocation.observe(this) {
            if (it != null) {
                for (data in it) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(data.lat as Double, data.lon as Double))
                            .title(data.name)
                            .snippet(data.description)
                    )
                }

                mMap.setOnInfoWindowClickListener { marker ->
                    val bundle = bundleOf(
                        "name" to marker.title,
                        "description" to marker.snippet
                    )
                    navController!!.navigate(R.id.action_mapsFragment_to_detailFragment, bundle)
                }
            }
        }
    }

    private fun setCameraMovement() {
        val cameraFocus = LatLng(-6.200000, 106.816666)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraFocus, 5f))
    }

    private fun setStyle() {
        try {
            val styleParsing =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!styleParsing) {
                Toast.makeText(context, "Style parsing failed", Toast.LENGTH_SHORT).show()
                Timber.d("Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Toast.makeText(context, "Can't find style. Error: ", Toast.LENGTH_SHORT).show()
            Timber.d("Can't find style. Error: $e")
        }
    }

}