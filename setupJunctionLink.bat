@echo off
for /d %%i in (.\versions\*) do (
mklink /j %%i\src\ .\shared\src
)