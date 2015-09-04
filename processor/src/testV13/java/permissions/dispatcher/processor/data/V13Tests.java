package permissions.dispatcher.processor.data;

import permissions.dispatcher.processor.base.BaseTest;

import static permissions.dispatcher.processor.data.Source.EMPTY_SOURCE;

public final class V13Tests {

    private V13Tests() {
    }
    public static final BaseTest NativeFragmentWithoutNeeds = new BaseTest() {
        @Override
        protected String getName() {
            return "MyFragment";
        }

        @Override
        protected String[] getActualSource() {
            return new String[] {
                    "package tests;",
                    "import android.Manifest;",
                    "import android.app.Fragment;",
                    "import permissions.dispatcher.RuntimePermissions;",
                    "@RuntimePermissions",
                    "public class MyFragment extends Fragment {",
                    "}"
            };
        }

        @Override
        protected String[] getExpectSource() {
            return EMPTY_SOURCE;
        }
    };

}
