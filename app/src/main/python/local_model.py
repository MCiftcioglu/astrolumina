import re

"""Simple local implementation to create an astrology comment.
The function expects the prompt text created by `GeminiHelper.createAstrologyPrompt`.
It extracts the Sun, Moon and Rising signs from the prompt and returns
a short interpretation string which is shown in `textViewResult`.
"""

def _extract_sign(prompt: str, label: str) -> str:
    match = re.search(label + r"\s*:\s*(\w+)", prompt, re.IGNORECASE)
    return match.group(1) if match else "Bilinmiyor"

def generate_astrology_comment(prompt: str) -> str:
    """Return a brief birth chart interpretation based on the given prompt."""
    sun = _extract_sign(prompt, "Güneş burcu")
    moon = _extract_sign(prompt, "Ay burcu")
    rising = _extract_sign(prompt, "Yükselen burç")

    return (
        f"Güneş burcu {sun}, Ay burcu {moon}, yükselen burcu {rising} olan "
        "kişiler dengeli bir yapıya sahiptir. Astrolojik potansiyelinizi "
        "ortaya çıkarmak için içgüdülerinize güvenebilirsiniz."
    )