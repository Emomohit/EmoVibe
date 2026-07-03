import os
import sys
import subprocess

try:
    from PIL import Image
except ImportError:
    subprocess.check_call([sys.executable, "-m", "pip", "install", "Pillow"])
    from PIL import Image

def resize_and_replace():
    icon_path = r"C:\Users\Mohit\.gemini\antigravity-ide\brain\5734abf1-cc64-4410-bc0f-3ee756e8204b\emovibes_premium_app_icon_1783007615383.png"
    base_dir = r"E:\Emo Games\EMOVibes\app\src\main\res"
    
    # Check if the original image exists
    if not os.path.exists(icon_path):
        print(f"Error: Could not find original image at {icon_path}")
        return

    # Load original image
    img = Image.open(icon_path).convert("RGBA")

    # Define sizes for mipmap directories
    sizes = {
        'mipmap-mdpi': 48,
        'mipmap-hdpi': 72,
        'mipmap-xhdpi': 96,
        'mipmap-xxhdpi': 144,
        'mipmap-xxxhdpi': 192,
    }

    # Replace ic_launcher.png, ic_launcher_foreground.png, ic_launcher_monochrome.png
    files_to_replace = ['ic_launcher.png', 'ic_launcher_foreground.png', 'ic_launcher_monochrome.png']

    for folder, size in sizes.items():
        folder_path = os.path.join(base_dir, folder)
        if not os.path.exists(folder_path):
            continue

        # Resize image
        resized_img = img.resize((size, size), Image.Resampling.LANCZOS)
        
        for file_name in files_to_replace:
            file_path = os.path.join(folder_path, file_name)
            if os.path.exists(file_path):
                resized_img.save(file_path, "PNG")
                print(f"Replaced {file_path}")

if __name__ == "__main__":
    resize_and_replace()
    print("All icons replaced successfully.")
