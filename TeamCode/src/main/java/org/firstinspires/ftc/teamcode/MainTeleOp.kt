package org.firstinspires.ftc.teamcode

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import dev.nextftc.hardware.driving.FieldCentric
import dev.nextftc.hardware.driving.MecanumDriverControlled
import dev.nextftc.hardware.impl.Direction
import dev.nextftc.hardware.impl.IMUEx
import dev.nextftc.hardware.impl.MotorEx
import org.firstinspires.ftc.teamcode.panels.Drawing
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

/**
 * An example TeleOp that is designed for a mecanum drivetrain using field centric for NextFTC and PedroPathing
 *
 * Feel free to use it as quickstart TeleOp code!
 *
 * @author Julian (D4LM) | 18592 Golden Prodigies 2026-2027
 * */
@TeleOp(name = "Main TeleOp")
class MainTeleOp : NextFTCOpMode() {
    init {
        addComponents(
            BulkReadComponent,
            PedroComponent(Constants::createFollower),
            BindingsComponent
        )
        telemetry = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
    }

    // Define motors here
    private val d0 = MotorEx("D0").brakeMode()
    private val d1 = MotorEx("D1").brakeMode()
    private val d2 = MotorEx("D3").brakeMode()
    private val d3 = MotorEx("D3").brakeMode()
    private val imu = IMUEx("imu", Direction.UP, Direction.FORWARD).zeroed()

    override fun onStartButtonPressed() {
        val driverControlled = MecanumDriverControlled(
            d0,
            d1,
            d2,
            d3,
            -Gamepads.gamepad1.leftStickY,
            Gamepads.gamepad1.leftStickX,
            Gamepads.gamepad1.rightStickX,
            FieldCentric(imu)
        )
        driverControlled()

        // Resets imu
        Gamepads.gamepad1.rightStickButton whenBecomesTrue {imu.zero()}

        // Slow mode
        Gamepads.gamepad1.b .toggleOnBecomesTrue()
            .whenBecomesTrue {
                driverControlled.scalar = 0.5
            } whenBecomesFalse {
                driverControlled.scalar = 1.0
        }
    }

    override fun onUpdate() {
        Drawing.drawDebug(PedroComponent.follower)

        ActiveOpMode.telemetry.update()
    }
}