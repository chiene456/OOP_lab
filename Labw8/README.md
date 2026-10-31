# Labw8 – Delegate & Event (Java)

Mô phỏng `delegate`/`event` của C# bằng Java: thiết bị (publisher) chỉ công bố sự kiện,
các subscriber (log, thông báo, bảo trì, bộ lọc) đăng ký từ bên ngoài.

Yêu cầu: JDK 17 trở lên.

## Cấu trúc

```
Labw8/
├── src/
│   ├── app/          Main.java, LabW08Test.java
│   ├── model/        DeviceStatus, AlertSeverity
│   ├── event/        DeviceStatusChangedEventArgs, DeviceAlertEventArgs
│   ├── exception/    InvalidDeviceReadingException
│   ├── device/       TeachingDevice, ProjectorRemote, AirConditionerRemote, WirelessMicrophone
│   ├── subscriber/   ConsoleNotificationService, DeviceAuditLogger, MaintenanceService,
│   │                 AlertFilter, FilteredAlertSubscriber
│   └── coordinator/  DeviceMonitoringCoordinator
├── results/          main-output.txt, test-output.txt
├── docs/             class-diagram.mmd (Mermaid)
└── bin/              (sinh ra khi biên dịch, không commit)
```

## Biên dịch và chạy bằng PowerShell

```powershell
New-Item -ItemType Directory -Force bin, results | Out-Null
$sources = @(Get-ChildItem src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
javac -encoding UTF-8 -d bin $sources
java "-Dfile.encoding=UTF-8" -cp bin app.Main | Out-File results/main-output.txt -Encoding utf8
java "-Dfile.encoding=UTF-8" -cp bin app.LabW08Test | Out-File results/test-output.txt -Encoding utf8
```

## Biên dịch và chạy bằng bash (Linux/macOS)

```bash
mkdir -p bin results
javac -encoding UTF-8 -d bin $(find src -name '*.java')
java -Dfile.encoding=UTF-8 -cp bin app.Main > results/main-output.txt
java -Dfile.encoding=UTF-8 -cp bin app.LabW08Test > results/test-output.txt
```
