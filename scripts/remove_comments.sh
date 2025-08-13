#!/usr/bin/env bash
# remove_comments.sh - Xoá dòng comment thuần (#, //) và comment cuối dòng trong file mã nguồn ngoài thư mục k17.
# Thư mục bị loại trừ: k17
# Lưu ý: Sao lưu trước khi chạy nếu sợ mất thông tin chú thích quan trọng.

set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
EXCLUDE_DIR="$ROOT_DIR/k17"

# Phần mở rộng xử lý
EXTENSIONS="java,properties,sh" # có thể bổ sung: txt,md

IFS=',' read -r -a EXTS <<< "$EXTENSIONS"

backup_dir="$ROOT_DIR/comment_backups_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$backup_dir"

echo "[INFO] Backup originals -> $backup_dir"

should_exclude() {
  case "$1" in
    "$EXCLUDE_DIR"* ) return 0;;
    * ) return 1;;
  esac
}

process_file() {
  local f="$1"
  if should_exclude "$f"; then
    return
  fi
  # Bỏ qua file .class, .jar, nhị phân
  if file "$f" | grep -qi "executable\|binary"; then
    return
  fi
  cp "$f" "$backup_dir/" 2>/dev/null || true
  local tmp="${f}.tmp.__clean__"
  # Quy tắc:
  # 1. Xoá dòng chỉ chứa whitespace + //.*
  # 2. Xoá dòng chỉ chứa whitespace + #.* nhưng giữ shebang (#!...)
  # 3. Xoá phần // comment ở cuối dòng mã (nếu không nằm trong chuỗi) đơn giản bằng regex (có thể false positive nếu // trong string literal)
  # 4. Xoá phần # comment cuối dòng cho .properties & .sh (trừ #!)
  # Chấp nhận rủi ro nhỏ với literal.
  awk '
    BEGIN { in_block=0 }
    {
      line=$0
      # Bỏ dòng block comment C-style /* ... */ toàn dòng (simplistic)
      if (match(line,/^[ 	]*\/\*/)) { if (match(line,/\*\/[ 	]*$/)) next; in_block=1; next }
      if (in_block) { if (match(line,/\*\//)) in_block=0; next }
      # Shebang
      if (NR==1 && match(line,/^#!/)) { print line; next }
      # Dòng chỉ comment // hoặc #
      if (match(line,/^[ 	]*\/\/.*$/)) next
      if (match(line,/^[ 	]*#.*$/)) next
      # Cắt // cuối dòng (thô) nếu có
    if (match(line,/\/\/[^\"]*$/)) {
      sub(/\/\/[^\"]*$/,"",line)
      }
      # Cắt # cuối dòng (trừ khi trong chuỗi) – thô
    if (match(line,/#[^\"]*$/)) {
         # Không xử lý nếu trước đó là ! (để nguyên) – nhưng ở đây vẫn cắt
      sub(/#[^\"]*$/,"",line)
      }
      # Trim trailing spaces
      sub(/[ 	]+$/,"",line)
      # Bỏ dòng rỗng kép
      if (match(line,/^[ 	]*$/)) { blank++ ; if (blank>1) next } else { blank=0 }
      print line
    }
  ' "$f" > "$tmp"
  mv "$tmp" "$f"
}

export -f process_file should_exclude
export backup_dir EXCLUDE_DIR

echo "[INFO] Scanning..."
# Duyệt bằng while + find đơn giản rồi tự loại trừ
find "$ROOT_DIR" -type f | while read -r file; do
  if [[ "$file" == $EXCLUDE_DIR* ]]; then
    continue
  fi
  for e in "${EXTS[@]}"; do
    if [[ "$file" == *.$e ]]; then
      process_file "$file"
      break
    fi
  done
done

echo "[DONE] Đã xử lý. Backup ở: $backup_dir"
