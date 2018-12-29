package mission_control.felixseip.com.missioncontrol.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;

public class PermissionController {

    private Context _context;
    private Activity _activity;
    private Map<String, Integer> _permissions = new HashMap<>();


    public PermissionController(Context context, Activity activity){
        _context = context;
        _activity = activity;
    }

    /**
     *
     *
     * */
    public void checkAppPermissions(){
        requestAppPermissions(getRequiredAppPermissions());
    }

    public boolean checkAppPermission(String permission){
        return _permissions.get(permission) != REQUESTED_PERMISSION_GRANTED;
    }

    /**
     *
     * */
    private void requestAppPermissions(String[] permissions){
        ActivityCompat.requestPermissions(_activity,
                permissions, 0);
    }

    private String[] getRequiredAppPermissions() {
        List<String> permissionsList = new ArrayList<>();

        try {
            PackageManager p = _context.getPackageManager();
            PackageInfo pi = p.getPackageInfo(_context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = pi.requestedPermissions;
            int[] permissionStatus = pi.requestedPermissionsFlags;

            Map<String, Integer> permsMap = new HashMap<>();
            for(int i = 0; i < permissions.length; i++){
                permsMap.put(permissions[i], permissionStatus[i]);
                if(permissionStatus[i] != REQUESTED_PERMISSION_GRANTED){
                    permissionsList.add(permissions[i]);
                }
            }
            setPermissionsMap(permsMap);
        }
        catch (PackageManager.NameNotFoundException ex){
            return null;
        }

        String[] permissions = new String[permissionsList.size()];
        for(int i = 0; i < permissionsList.size(); i++){
            permissions[i] = permissionsList.get(i);
        }

        return permissions;
    }

    private void setPermissionsMap(Map<String, Integer> permissions){
        _permissions = permissions;
    }

}
