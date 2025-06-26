import datetime
import builtins

SIGNS = [
    "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
    "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık",
]

def _sun_sign(day, month):
    if month == 1:
        return "Oğlak" if day < 20 else "Kova"
    elif month == 2:
        return "Kova" if day < 19 else "Balık"
    elif month == 3:
        return "Balık" if day < 21 else "Koç"
    elif month == 4:
        return "Koç" if day < 20 else "Boğa"
    elif month == 5:
        return "Boğa" if day < 21 else "İkizler"
    elif month == 6:
        return "İkizler" if day < 21 else "Yengeç"
    elif month == 7:
        return "Yengeç" if day < 23 else "Aslan"
    elif month == 8:
        return "Aslan" if day < 23 else "Başak"
    elif month == 9:
        return "Başak" if day < 23 else "Terazi"
    elif month == 10:
        return "Terazi" if day < 23 else "Akrep"
    elif month == 11:
        return "Akrep" if day < 22 else "Yay"
    elif month == 12:
        return "Yay" if day < 22 else "Oğlak"
    else:
        return "Bilinmiyor"

def _cycle_sign(index):
    return SIGNS[index % 12]

def calculate_signs(date_str, time_str, latitude, longitude):
    try:
        day, month, year = map(builtins.int, date_str.split("/"))
        hour, minute = map(builtins.int, time_str.split(":"))

        sun_sign = _sun_sign(day, month)

        doy = datetime.date(year, month, day).timetuple().tm_yday
        moon_index = (doy + builtins.int(builtins.abs(latitude))) % 12
        moon_sign = _cycle_sign(moon_index)

        rising_index = (hour // 2 + builtins.int(builtins.abs(longitude))) % 12
        rising_sign = _cycle_sign(rising_index)

        return f"{sun_sign},{moon_sign},{rising_sign}"
    except Exception:
        return "Bilinmiyor,Bilinmiyor,Bilinmiyor"