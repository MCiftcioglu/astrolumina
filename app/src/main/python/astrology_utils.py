"""Simple astrology helper used via Chaquopy.

This module avoids heavy dependencies like ``skyfield`` and provides a very
basic sign calculation so the app can run without additional Python packages.
The results are not astronomically accurate but allow the Kotlin side to obtain
deterministic Sun, Moon and rising signs based on the user's birth details.
"""

from datetime import date

SIGNS = [
    "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
    "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık",
]

def _sun_sign(day: int, month: int) -> str:
"""Return Western zodiac (Sun sign) in Turkish for given day/month."""
if month == 1:
    return "Oğlak" if day < 20 else "Kova"
if month == 2:
    return "Kova" if day < 19 else "Balık"
if month == 3:
    return "Balık" if day < 21 else "Koç"
if month == 4:
    return "Koç" if day < 20 else "Boğa"
if month == 5:
    return "Boğa" if day < 21 else "İkizler"
if month == 6:
    return "İkizler" if day < 22 else "Yengeç"
if month == 7:
    return "Yengeç" if day < 23 else "Aslan"
if month == 8:
    return "Aslan" if day < 23 else "Başak"
if month == 9:
    return "Başak" if day < 23 else "Terazi"
if month == 10:
    return "Terazi" if day < 23 else "Akrep"
if month == 11:
    return "Akrep" if day < 22 else "Yay"
if month == 12:
    return "Yay" if day < 22 else "Oğlak"
return "Bilinmiyor"

def _cycle_sign(index: int) -> str:
    return SIGNS[index % 12]

    def calculate_signs(date_str: str, time_str: str, latitude: float, longitude: float) -> str:
"""Return a comma separated string of Sun, Moon and rising signs.

The algorithm is intentionally simple and does not rely on external
astronomy libraries. It provides deterministic but approximate results so
that the Android app can work offline.
"""
try:
    # Parse provided date and time
    day, month, year = map(int, date_str.split("/"))
    hour, minute = map(int, time_str.split(":"))

    # Sun sign from birth date
    sun_sign = _sun_sign(day, month)

    # Moon sign approximated by day of year and latitude
    doy = date(year, month, day).timetuple().tm_yday
    moon_index = (doy + int(abs(latitude))) % 12
    moon_sign = _cycle_sign(moon_index)

    # Rising sign approximated by birth hour and longitude
    rising_index = (hour // 2 + int(abs(longitude))) % 12
    rising_sign = _cycle_sign(rising_index)

    return f"{sun_sign},{moon_sign},{rising_sign}"
except Exception:
    return "Bilinmiyor,Bilinmiyor,Bilinmiyor"
