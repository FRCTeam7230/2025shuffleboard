package edu.wpi.first.shuffleboard.plugin.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.wpi.first.shuffleboard.api.PropertyParser;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.data.DataTypes;
import edu.wpi.first.shuffleboard.api.json.ElementTypeAdapter;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.prefs.Group;
import edu.wpi.first.shuffleboard.api.prefs.Setting;
import edu.wpi.first.shuffleboard.api.tab.TabInfo;
import edu.wpi.first.shuffleboard.api.util.PreferencesUtils;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.LayoutClass;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.AccelerometerType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.AlertsType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.AnalogInputType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.BasicSubsystemType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.CommandType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.DifferentialDriveType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.EncoderType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.FieldTypeRed;
import edu.wpi.first.shuffleboard.plugin.base.data.types.FieldTypeBlu;
import edu.wpi.first.shuffleboard.plugin.base.data.types.FmsInfoType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.GyroType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.MecanumDriveType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.PIDCommandType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.PIDControllerType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.ProfiledPIDControllerType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.PowerDistributionType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.QuadratureEncoderType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.RelayType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.RobotPreferencesType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.SendableChooserType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.SpeedControllerType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.SubsystemType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.ThreeAxisAccelerometerType;
import edu.wpi.first.shuffleboard.plugin.base.data.types.UltrasonicType;
import edu.wpi.first.shuffleboard.plugin.base.layout.GridLayout;
import edu.wpi.first.shuffleboard.plugin.base.layout.GridLayoutSaver;
import edu.wpi.first.shuffleboard.plugin.base.layout.ListLayout;
import edu.wpi.first.shuffleboard.plugin.base.layout.SubsystemLayout;
import edu.wpi.first.shuffleboard.plugin.base.widget.AccelerometerWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.AlertsWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.BasicFmsInfoWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.BasicSubsystemWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.BooleanBoxWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.ComboBoxChooserWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.CommandWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.DifferentialDriveWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.EncoderWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.FieldWidgetBlu;
import edu.wpi.first.shuffleboard.plugin.base.widget.FieldWidgetRed;
import edu.wpi.first.shuffleboard.plugin.base.widget.GraphWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.GyroWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.MecanumDriveWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.NumberBarWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.NumberSliderWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.PIDCommandWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.PIDControllerWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.ProfiledPIDControllerWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.PowerDistributionPanelWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.RelayWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.RobotPreferencesWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.SimpleDialWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.SpeedControllerWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.SplitButtonChooserWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.TextViewWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.ThreeAxisAccelerometerWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.ToggleButtonWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.ToggleSwitchWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.UltrasonicWidget;
import edu.wpi.first.shuffleboard.plugin.base.widget.VoltageViewWidget;
import javafx.beans.InvalidationListener;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

@Description(
    group = "edu.wpi.first.shuffleboard",
    name = "Base",
    version = "1.3.7",
    summary = "Defines all the WPILib data types and stock widgets"
)
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class BasePlugin extends Plugin {
  private final Preferences preferences = Preferences.userNodeForPackage(getClass());
  private GraphWidget.Updater updater;
  private InvalidationListener graphSaver;

  @Override
  public void onLoad() {
    this.updater = new GraphWidget.Updater();

    this.graphSaver = n -> PreferencesUtils.save(updater.graphUpdateRateProperty(), preferences);
    PreferencesUtils.read(updater.graphUpdateRateProperty(), preferences);
    updater.graphUpdateRateProperty().addListener(graphSaver);
  }

  @Override
  public void onUnload() {
    updater.graphUpdateRateProperty().removeListener(graphSaver);
    updater.close();
  }

  @Override
  public List<DataType> getDataTypes() {
    return ImmutableList.of(
        AnalogInputType.Instance,
        PowerDistributionType.Instance,
        EncoderType.Instance,
        QuadratureEncoderType.Instance,
        RobotPreferencesType.Instance,
        SendableChooserType.Instance,
        SpeedControllerType.Instance,
        SubsystemType.Instance,
        BasicSubsystemType.Instance,
        CommandType.Instance,
        PIDCommandType.Instance,
        PIDControllerType.Instance,
        ProfiledPIDControllerType.Instance,
        AccelerometerType.Instance,
        ThreeAxisAccelerometerType.Instance,
        GyroType.Instance,
        RelayType.Instance,
        MecanumDriveType.Instance,
        DifferentialDriveType.Instance,
        FmsInfoType.Instance,
        UltrasonicType.Instance,
        FieldTypeRed.Instance,
        FieldTypeBlu.Instance,
        AlertsType.Instance
    );
  }

  @Override
  public List<ComponentType> getComponents() {
    return ImmutableList.of(
        WidgetType.forAnnotatedWidget(BooleanBoxWidget.class),
        WidgetType.forAnnotatedWidget(ToggleButtonWidget.class),
        WidgetType.forAnnotatedWidget(ToggleSwitchWidget.class),
        WidgetType.forAnnotatedWidget(NumberSliderWidget.class),
        WidgetType.forAnnotatedWidget(NumberBarWidget.class),
        WidgetType.forAnnotatedWidget(SimpleDialWidget.class),
        WidgetType.forAnnotatedWidget(GraphWidget.class),
        WidgetType.forAnnotatedWidget(TextViewWidget.class),
        WidgetType.forAnnotatedWidget(VoltageViewWidget.class),
        WidgetType.forAnnotatedWidget(PowerDistributionPanelWidget.class),
        WidgetType.forAnnotatedWidget(ComboBoxChooserWidget.class),
        WidgetType.forAnnotatedWidget(SplitButtonChooserWidget.class),
        WidgetType.forAnnotatedWidget(EncoderWidget.class),
        WidgetType.forAnnotatedWidget(RobotPreferencesWidget.class),
        WidgetType.forAnnotatedWidget(SpeedControllerWidget.class),
        WidgetType.forAnnotatedWidget(CommandWidget.class),
        WidgetType.forAnnotatedWidget(BasicSubsystemWidget.class),
        WidgetType.forAnnotatedWidget(PIDCommandWidget.class),
        WidgetType.forAnnotatedWidget(AccelerometerWidget.class),
        WidgetType.forAnnotatedWidget(ThreeAxisAccelerometerWidget.class),
        WidgetType.forAnnotatedWidget(PIDControllerWidget.class),
        WidgetType.forAnnotatedWidget(ProfiledPIDControllerWidget.class),
        WidgetType.forAnnotatedWidget(GyroWidget.class),
        WidgetType.forAnnotatedWidget(RelayWidget.class),
        WidgetType.forAnnotatedWidget(DifferentialDriveWidget.class),
        WidgetType.forAnnotatedWidget(MecanumDriveWidget.class),
        WidgetType.forAnnotatedWidget(BasicFmsInfoWidget.class),
        WidgetType.forAnnotatedWidget(UltrasonicWidget.class),
        WidgetType.forAnnotatedWidget(FieldWidgetRed.class),
        WidgetType.forAnnotatedWidget(FieldWidgetBlu.class),
        WidgetType.forAnnotatedWidget(AlertsWidget.class),
        new LayoutClass<>("List Layout", ListLayout.class),
        new LayoutClass<>("Grid Layout", GridLayout.class),
        createSubsystemLayoutType()
    );
  }

  @Override
  public Map<DataType, ComponentType> getDefaultComponents() {
    return ImmutableMap.<DataType, ComponentType>builder()
        .put(DataTypes.Boolean, WidgetType.forAnnotatedWidget(BooleanBoxWidget.class))
        .put(DataTypes.Number, WidgetType.forAnnotatedWidget(TextViewWidget.class))
        .put(DataTypes.String, WidgetType.forAnnotatedWidget(TextViewWidget.class))
        .put(AnalogInputType.Instance, WidgetType.forAnnotatedWidget(VoltageViewWidget.class))
        .put(PowerDistributionType.Instance, WidgetType.forAnnotatedWidget(PowerDistributionPanelWidget.class))
        .put(SendableChooserType.Instance, WidgetType.forAnnotatedWidget(ComboBoxChooserWidget.class))
        .put(EncoderType.Instance, WidgetType.forAnnotatedWidget(EncoderWidget.class))
        .put(QuadratureEncoderType.Instance, WidgetType.forAnnotatedWidget(EncoderWidget.class))
        .put(RobotPreferencesType.Instance, WidgetType.forAnnotatedWidget(RobotPreferencesWidget.class))
        .put(SpeedControllerType.Instance, WidgetType.forAnnotatedWidget(SpeedControllerWidget.class))
        .put(CommandType.Instance, WidgetType.forAnnotatedWidget(CommandWidget.class))
        .put(PIDCommandType.Instance, WidgetType.forAnnotatedWidget(PIDCommandWidget.class))
        .put(PIDControllerType.Instance, WidgetType.forAnnotatedWidget(PIDControllerWidget.class))
        .put(ProfiledPIDControllerType.Instance, WidgetType.forAnnotatedWidget(ProfiledPIDControllerWidget.class))
        .put(AccelerometerType.Instance, WidgetType.forAnnotatedWidget(AccelerometerWidget.class))
        .put(ThreeAxisAccelerometerType.Instance, WidgetType.forAnnotatedWidget(ThreeAxisAccelerometerWidget.class))
        .put(GyroType.Instance, WidgetType.forAnnotatedWidget(GyroWidget.class))
        .put(RelayType.Instance, WidgetType.forAnnotatedWidget(RelayWidget.class))
        .put(DifferentialDriveType.Instance, WidgetType.forAnnotatedWidget(DifferentialDriveWidget.class))
        .put(MecanumDriveType.Instance, WidgetType.forAnnotatedWidget(MecanumDriveWidget.class))
        .put(FmsInfoType.Instance, WidgetType.forAnnotatedWidget(BasicFmsInfoWidget.class))
        .put(UltrasonicType.Instance, WidgetType.forAnnotatedWidget(UltrasonicWidget.class))
        .put(BasicSubsystemType.Instance, WidgetType.forAnnotatedWidget(BasicSubsystemWidget.class))
        .put(FieldTypeRed.Instance, WidgetType.forAnnotatedWidget(FieldWidgetRed.class))
        .put(FieldTypeBlu.Instance, WidgetType.forAnnotatedWidget(FieldWidgetBlu.class))
        .put(AlertsType.Instance, WidgetType.forAnnotatedWidget(AlertsWidget.class))
        .put(SubsystemType.Instance, createSubsystemLayoutType())
        .build();
  }

  @Override
  public Set<PropertyParser<?>> getPropertyParsers() {
    return Set.of(
        PropertyParser.forEnum(ThreeAxisAccelerometerWidget.Range.class),
        PropertyParser.forEnum(UltrasonicWidget.Unit.class)
    );
  }

  private static LayoutClass<SubsystemLayout> createSubsystemLayoutType() {
    return new LayoutClass<SubsystemLayout>("Subsystem Layout", SubsystemLayout.class) {
      @Override
      public Set<DataType> getDataTypes() {
        return ImmutableSet.of(SubsystemType.Instance);
      }
    };
  }

  @Override
  public List<TabInfo> getDefaultTabInfo() {
    return ImmutableList.of(
        TabInfo.builder().name("SmartDashboard").autoPopulate().sourcePrefix("SmartDashboard/").build(),
        TabInfo.builder().name("LiveWindow").autoPopulate().sourcePrefix("LiveWindow/").build()
    );
  }

  @Override
  public List<ElementTypeAdapter<?>> getCustomTypeAdapters() {
    return ImmutableList.of(
        GridLayoutSaver.Instance
    );
  }

  @Override
  public List<Group> getSettings() {
    return ImmutableList.of(
            Group.of("Graph settings",
                    Setting.of("Graph Update Rate",
                            "How many times a second graph widgets update at. "
                                    + "Faster update rates may cause performance issues",
                            updater.graphUpdateRateProperty()
                    )
            )
    );
  }
}
