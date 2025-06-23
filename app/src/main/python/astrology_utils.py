from datetime import date

SIGNS = [
    "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
    "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık",
]

def _sun_sign(day: int, month: int) -> str:
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
        return "İkizler" if day < 21 else "Yengeç"
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
    try:
        day, month, year = map(int, date_str.split("/"))
        hour, minute = map(int, time_str.split(":"))

        sun_sign = _sun_sign(day, month)

        doy = date(year, month, day).timetuple().tm_yday
        moon_index = (doy + int(abs(latitude))) % 12
        moon_sign = _cycle_sign(moon_index)

        rising_index = (hour // 2 + int(abs(longitude))) % 12
        rising_sign = _cycle_sign(rising_index)

        return f"{sun_sign},{moon_sign},{rising_sign}"
    except Exception:
        return "Bilinmiyor,Bilinmiyor,Bilinmiyor"
