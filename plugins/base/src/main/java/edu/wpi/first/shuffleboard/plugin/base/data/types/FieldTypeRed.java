package edu.wpi.first.shuffleboard.plugin.base.data.types;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import edu.wpi.first.shuffleboard.plugin.base.data.FieldData;

import java.util.Map;
import java.util.function.Function;

public final class FieldTypeRed extends ComplexDataType<FieldData> {
  public static final FieldTypeRed Instance = new FieldTypeRed();

  private FieldTypeRed() {
    super("Field2d", FieldData.class);
  }

  @Override
  public Function<Map<String, Object>, FieldData> fromMap() {
    return FieldData::new;
  }

  @Override
  public FieldData getDefaultValue() {
    return new FieldData(new FieldData.SimplePose2d(0, 0, 0), Map.of());
  }
}
