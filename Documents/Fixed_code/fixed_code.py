from flask import request, jsonify
from sqlalchemy.exc import SQLAlchemyError
from models import Product, Inventory, db

@app.route('/api/products', methods=['POST'])
def create_product():
    data = request.get_json()

    # Validate required fields
    required_fields = ['name', 'sku', 'price', 'warehouse_id', 'initial_quantity']
    missing = [field for field in required_fields if field not in data]
    if missing:
        return jsonify({"error": f"Missing fields: {', '.join(missing)}"}), 400

    # Check SKU uniqueness
    existing_product = Product.query.filter_by(sku=data['sku']).first()
    if existing_product:
        return jsonify({"error": "SKU must be unique"}), 409

    try:
        # Convert price to float (handle decimal)
        price = float(data['price'])

        # Start transaction
        product = Product(
            name=data['name'],
            sku=data['sku'],
            price=price,
            warehouse_id=data['warehouse_id']
        )
        db.session.add(product)
        db.session.flush()  # Get product.id before commit

        inventory = Inventory(
            product_id=product.id,
            warehouse_id=data['warehouse_id'],
            quantity=data['initial_quantity']
        )
        db.session.add(inventory)
        db.session.commit()

        return jsonify({"message": "Product created", "product_id": product.id}), 201

    except ValueError:
        db.session.rollback()
        return jsonify({"error": "Invalid price format"}), 400
    except SQLAlchemyError as e:
        db.session.rollback()
        return jsonify({"error": "Database error", "details": str(e)}), 500
    except Exception as e:
        db.session.rollback()
        return jsonify({"error": "Something went wrong", "details": str(e)}), 500
